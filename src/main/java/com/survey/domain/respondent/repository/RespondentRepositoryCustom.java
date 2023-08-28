package com.survey.domain.respondent.repository;

import com.survey.domain.respondent.dto.RespondentEssayDto;

import java.util.List;

public interface RespondentRepositoryCustom {
    List<RespondentEssayDto> findAnswerByTypeIsEssayAndSurveyId(Long surveyId);
}
