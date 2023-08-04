package com.survey.domain.options.service;

import com.survey.domain.options.dto.OptionsRequestDto;
import com.survey.domain.options.dto.QuestionOptionsRequestDto;
import com.survey.domain.options.entity.Options;
import com.survey.domain.options.repository.OptionRepository;
import com.survey.domain.question.dto.QuestionRequestDto;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.survey.domain.question.entity.QuestionType.FIVE_MULTIPLE_CHOICE;

@Transactional
@RequiredArgsConstructor
@Service
public class OptionsService {
    private final OptionRepository optionRepository;
    private final QuestionService questionService;

    public void createQuestionAndOptions(List<QuestionOptionsRequestDto> requests, Long surveyId, String email) {
        int questionSequence = 1;
        for (QuestionOptionsRequestDto request : requests) {
            QuestionRequestDto questionRequestDto = request.getQuestion();
            Questions questions = questionService.createQuestion(questionRequestDto, surveyId, email, questionSequence);
            questionSequence += 1;
            // 5지선다인 경우
            if(questions.getQuestionType().equals(FIVE_MULTIPLE_CHOICE)) {
                List<OptionsRequestDto> optionsRequestDtos = request.getOptions();
                createOptions(questions.getId(), optionsRequestDtos);
            }
            else {
                throw new IllegalArgumentException("서술형일 경우 추가 불가");
            }
        }
    }

    private void createOptions(Long questionId, List<OptionsRequestDto> optionsRequestDtos) {
        int optionsSequence = 1;
        for (OptionsRequestDto option : optionsRequestDtos) {
            Options options = Options.builder()
                    .option(option.getOption())
                    .questionId(questionId)
                    .sequence(optionsSequence)
                    .build();
            optionRepository.save(options);
            optionsSequence += 1;
        }
    }
}
