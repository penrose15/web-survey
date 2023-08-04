package com.survey.domain.options.service;

import com.survey.domain.options.dto.QuestionOptionsRequestDto;
import com.survey.domain.options.repository.OptionRepository;
import com.survey.domain.question.dto.QuestionRequestDto;
import com.survey.domain.question.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class OptionsService {
    private final OptionRepository optionRepository;
    private final QuestionService questionService;

    public boolean createQuestions(List<QuestionOptionsRequestDto> requests, Long surveyId, String email) {
        int questionSequence = 1;
        for (QuestionOptionsRequestDto request : requests) {
            QuestionRequestDto questionRequestDto = request.getQuestion();
            Long questionId = questionService.createQuestion(request.getQuestion(), surveyId, email);

        }
    }
}
