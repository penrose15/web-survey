package com.survey.domain.question.service;

import com.survey.domain.options.dto.*;
import com.survey.domain.options.entity.Options;
import com.survey.domain.options.service.OptionsService;
import com.survey.domain.question.dto.*;
import com.survey.domain.question.entity.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.survey.domain.question.entity.QuestionType.FIVE_MULTIPLE_CHOICE;

@Transactional
@RequiredArgsConstructor
@Service
public class QuestionAndOptionService {
    private final QuestionService questionService;
    private final OptionsService optionsService;

    public void createQuestionAndOptions(List<QuestionOptionsRequestDto> requests, Long surveyId) {


        int questionSequence = 1;
        for (QuestionOptionsRequestDto request : requests) {
            QuestionRequestDto questionRequestDto = request.getQuestion();
            Questions questions = questionService.createQuestion(questionRequestDto, surveyId, questionSequence);
            questionSequence += 1;
            // 5지선다인 경우
            if(questions.getQuestionType().equals(FIVE_MULTIPLE_CHOICE)) {
                List<OptionsRequestDto> optionsRequestDtos = request.getOptions();
                optionsService.createOptions(questions.getId(), optionsRequestDtos);
            }
            else {
                throw new IllegalArgumentException("서술형일 경우 추가 불가");
            }
        }
    }

    public void updateQuestionsAndOptions(Long surveyId, List<QuestionOptionsRequestDto> requests, String email) {
        questionService.deleteBySurveyId(surveyId);
        createQuestionAndOptions(requests, surveyId);

    }

    public Page<QuestionOptionResponseDto> getQuestionsAndOptionsBySurveyId(int page, int size, Long surveyId) {
        Page<Questions> questionsPage = questionService.findPageBySurveyId(surveyId, page, size);

        List<Long> questionIds = getQuestionIds(questionsPage);

        Map<Long, List<OptionsResponseDto>> optionsMap = getOptionsMap(questionIds);

        List<QuestionOptionResponseDto> questionOptionResponseDtos = questionsPage.getContent().stream()
                .map(q -> QuestionOptionResponseDto.builder()
                        .id(q.getId())
                        .title(q.getTitle())
                        .surveyId(q.getSurveyId())
                        .imageUrl(q.getImageUrl())
                        .questionType(q.getQuestionType())
                        .questionSequence(q.getSequence())
                        .optionsList(optionsMap.getOrDefault(q.getId(), Collections.emptyList()))
                        .build()).toList();

        return new PageImpl<>(questionOptionResponseDtos, questionsPage.getPageable(), questionsPage.getTotalElements());
    }

    private Map<Long, List<OptionsResponseDto>> getOptionsMap(List<Long> questionIds) {
        return optionsService.findAllByQuestionIds(questionIds).stream()
                .collect(Collectors.groupingBy(
                        Options::getQuestionId,
                        Collectors.mapping(o -> OptionsResponseDto.builder()
                                .id(o.getId())
                                .options(o.getOption())
                                .optionSequence(o.getSequence())
                                .build(), Collectors.toList())
                ));
    }

    private List<Long> getQuestionIds(Page<Questions> questionsPage) {
        return questionsPage.getContent().stream()
                .mapToLong(Questions::getId)
                .boxed().toList();
    }

    public QuestionOptionResponseDto getQuestionAndOptionsByQuestionId(Long questionId) {
        Questions question = questionService.findById(questionId);
        List<Options> optionsList = optionsService.findAllByQuestionsId(questionId);

        List<OptionsResponseDto> optionsResponseDtoList = optionsList.stream()
                .map(o -> OptionsResponseDto.builder()
                        .id(o.getId())
                        .options(o.getOption())
                        .optionSequence(o.getSequence())
                        .build()).toList();

        return QuestionOptionResponseDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .questionType(question.getQuestionType())
                .surveyId(question.getSurveyId())
                .imageUrl(question.getImageUrl())
                .questionSequence(question.getSequence())
                .optionsList(optionsResponseDtoList)
                .build();
    }

    public void deleteByQuestionId(Long questionId) {
        optionsService.deleteOptionsByQuestionId(questionId);
        questionService.deleteById(questionId);
    }


    public void deleteAllBySurveyId(Long surveyId) {
        List<Questions> questionsList = questionService.findBySurveyId(surveyId);
        List<Long> questionIds = questionsList.stream().mapToLong(Questions::getId)
                .boxed().toList();
        optionsService.deleteOptionsByQuestionIds(questionIds);
        questionService.deleteBySurveyId(surveyId);
    }

}
