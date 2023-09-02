package com.survey.domain.respondent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RespondentRequestDto {

    private String answer;
    @NotNull
    private Long questionId;
    private Long optionId;
    private Integer optionSequence;
}
