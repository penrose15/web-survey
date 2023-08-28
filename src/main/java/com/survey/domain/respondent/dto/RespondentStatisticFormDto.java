package com.survey.domain.respondent.dto;


import com.survey.domain.question.dto.QuestionAndOptionStatisticDto;
import com.survey.domain.question.dto.QuestionStatisticResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RespondentStatisticFormDto {
    private Long surveyId;
    private String surveyName;
    private String startAt;
    private String endAt;
    private Integer userLimit;
    List<QuestionAndOptionStatisticDto> statistics;

    @Builder
    public RespondentStatisticFormDto(Long surveyId, String surveyName, String startAt, String endAt, Integer userLimit, List<QuestionAndOptionStatisticDto> statistics) {
        this.surveyId = surveyId;
        this.surveyName = surveyName;
        this.startAt = startAt;
        this.endAt = endAt;
        this.userLimit = userLimit;
        this.statistics = statistics;
    }
}
