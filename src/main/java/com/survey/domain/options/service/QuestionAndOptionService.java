package com.survey.domain.options.service;

import com.survey.domain.options.dto.*;
import com.survey.domain.question.dto.QuestionRequestDto;
import com.survey.domain.question.dto.QuestionUpdateDto;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.survey.domain.question.entity.QuestionType.FIVE_MULTIPLE_CHOICE;

@Transactional
@RequiredArgsConstructor
@Service
public class QuestionAndOptionService {
    private final QuestionService questionService;
    private final OptionsService optionsService;

    public void createQuestionAndOptions(List<QuestionOptionsRequestDto> requests, Long surveyId, String email) {

        for (QuestionOptionsRequestDto request : requests) {
            QuestionRequestDto questionRequestDto = request.getQuestion();
            Questions questions = questionService.createQuestion(questionRequestDto, surveyId, email);

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

    public void updateQuestionsAndOptions(Long surveyId, QuestionAndOptionUpdateDto updateRequest, String email) {
        List<QuestionOptionsRequestDto> questionOptionsRequestDtos = updateRequest.getAddQuestionAndOptions();
        createQuestionAndOptions(questionOptionsRequestDtos, surveyId, email);

        List<QuestionUpdateDto> questionUpdateDtos = updateRequest.getQuestionUpdateDtos();

        for (QuestionUpdateDto questionUpdateDto : questionUpdateDtos) {
            questionService.updateQuestion(questionUpdateDto.getQuestionRequestDto(), questionUpdateDto.getQuestionId(),surveyId, email);
        }

        List<OptionCreateDto> optionCreateDtos = updateRequest.getOptionsCreateDtos();
        for (OptionCreateDto optionCreateDto : optionCreateDtos) {
            optionsService.createOption(optionCreateDto.getQuestionId(), optionCreateDto.getOptionsRequestDto());
        }

        List<OptionUpdateDto> optionUpdateDtos = updateRequest.getOptionUpdateDtos();
        for (OptionUpdateDto optionUpdateDto : optionUpdateDtos) {
            optionsService.updateOptions(optionUpdateDto.getOptionId(), optionUpdateDto.getOptionsDto());
        }

        List<Long> deleteQuestionDto = updateRequest.getDeleteQuestionDto();
        for (Long questionId : deleteQuestionDto) {
            questionService.deleteById(questionId);
        }

        List<Long> deleteOptionsDto = updateRequest.getDeleteOptionsDto();
        for (Long optionId : deleteOptionsDto) {
            optionsService.deleteOptions(optionId);
        }
    }
}
