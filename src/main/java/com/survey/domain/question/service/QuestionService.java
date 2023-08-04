package com.survey.domain.question.service;

import com.survey.domain.question.dto.QuestionRequestDto;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.repository.QuestionRepository;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.service.SurveyFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final SurveyFindService surveyFindService;

    public Questions createQuestion(QuestionRequestDto questionRequestDto, Long surveyId, String email, int sequence) {

        Questions questions = Questions.builder()
                .title(questionRequestDto.getTitle())
                .surveyId(surveyId)
                .questionSequence(sequence)
                .questionType(QuestionType.getQuestionType(questionRequestDto.getQuestionType()))
                .build();

        questions = questionRepository.save(questions);
        return questions;
    }

    public Long updateQuestion(QuestionRequestDto questionRequestDto, Long questionId, Long surveyId, String email) {
        surveyFindService.findByIdAndEmail(email, surveyId);
        Questions questions = findById(questionId);
        questions.updateQuestion(questions.getTitle(), questionRequestDto.getQuestionType());

        questions = questionRepository.save(questions);

        return questions.getId();
    }

    //조회는 options와 같이 보여줘야 할 것 같아서 패스

    private Questions findById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 question"));
    }
}
