package com.survey.domain.options.repository;

import com.survey.domain.options.dto.FiveMultipleChoiceStatisticDto;

import java.util.List;

public interface OptionsRepositoryCustom {
    List<FiveMultipleChoiceStatisticDto> findFiveMultipleChoiceStatisticByQuestionIds(Long surveyId);
}
