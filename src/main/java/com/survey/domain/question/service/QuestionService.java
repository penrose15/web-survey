package com.survey.domain.question.service;

import com.survey.domain.options.repository.OptionsRepository;
import com.survey.domain.question.dto.QuestionDto;
import com.survey.domain.question.dto.QuestionRequestDto;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.repository.QuestionsRepository;
import com.survey.domain.survey.service.SurveyFindService;
import com.survey.global.aws.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionsRepository questionsRepository;
    private final OptionsRepository optionsRepository;
    private final SurveyFindService surveyFindService;
    private final AwsS3Service awsS3Service;

    public Questions createQuestion(QuestionRequestDto questionRequestDto, Long surveyId, Integer sequence) {
        String filename = null;
        String url = null;

        if(questionRequestDto.getMultipartFile() != null) {
            url = awsS3Service.uploadFile(questionRequestDto.getMultipartFile());
            filename = awsS3Service.getFileName(questionRequestDto.getMultipartFile());
        }

        QuestionDto question = questionRequestDto.getQuestionDto();

        Questions questions = Questions.builder()
                .title(question.getTitle())
                .surveyId(surveyId)
                .questionType(QuestionType.getQuestionType(question.getQuestionType()))
                .imageName(filename)
                .imageUrl(url)
                .isEssential(question.isEssential())
                .build();
        questions.setSequence(sequence);
        questions = questionsRepository.save(questions);
        return questions;
    }

    public Long updateQuestion(QuestionRequestDto questionRequestDto, Long questionId, Long surveyId, String email) {
        surveyFindService.findByIdAndEmail(email, surveyId);
        Questions questions = findById(questionId);
        QuestionDto questionDto = questionRequestDto.getQuestionDto();

        questions.updateQuestion(questions.getTitle(), questionDto.getQuestionType(), questionDto.isEssential());

        questions = questionsRepository.save(questions);

        return questions.getId();
    }

    public List<Questions> findBySurveyId(Long surveyId) {
        return questionsRepository.findListBySurveyId(surveyId);
    }

    public Page<Questions> findPageBySurveyId(Long surveyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        return questionsRepository.findPageBySurveyId(pageable, surveyId);
    }

    //조회는 options와 같이 보여줘야 할 것 같아서 패스

    public Questions findById(Long questionId) {
        return questionsRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 question"));
    }


    public void deleteById(Long questionId) {
        findById(questionId); //validation
        questionsRepository.deleteById(questionId);
    }

    public void deleteQuestionByQuestionIdList(List<Long> questionIdList) {
        questionsRepository.deleteAllById(questionIdList);
    }

    public void deleteBySurveyId(Long surveyId) {
        questionsRepository.deleteQuestionsBySurveyId(surveyId);
    }

    public int countQuestionsBySurveyId(Long surveyId) {
        return questionsRepository.countQuestionsBySurveyId(surveyId);
    }

    public List<Questions> findBySurveyAndIsEssential(Long surveyId) {
        return questionsRepository.findBySurveyIdAndIsEssential(surveyId);
    }
}
