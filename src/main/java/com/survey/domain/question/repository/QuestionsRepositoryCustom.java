package com.survey.domain.question.repository;

import com.survey.domain.question.dto.QuestionStatisticResponseDto;
import com.survey.domain.question.entity.Questions;

import java.util.List;

public interface QuestionsRepositoryCustom {
    List<QuestionStatisticResponseDto> findQuestionStatisticsByTypeIsFiveMultipleChoice(Long surveyId);
    List<QuestionStatisticResponseDto> findQuestionStatisticsByTypeIsEssay(Long surveyId);
    List<Questions> findBySurveyAndQuestionTypeIsFiveMultipleChoice(Long surveyId);
    List<Questions> findBySurveyAndQuestionTypeIsEssay(Long surveyId);
}
