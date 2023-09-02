package com.survey.domain.respondent.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RespondentUpdateDto {
    private Long RespondentId;
    private String answer;
    private Long optionId;
    private Integer optionSequence;
}
