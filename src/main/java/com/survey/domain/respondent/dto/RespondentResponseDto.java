package com.survey.domain.respondent.dto;

import com.survey.domain.options.dto.OptionsResponseDto;
import com.survey.domain.question.dto.QuestionsResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RespondentResponseDto {
    private QuestionsResponseDto questions;
    private List<OptionsResponseDto> options;
    private Long respondentId;
    private String answer;
    private Long optionId;
    private Integer optionSequence;

    @Builder
    public RespondentResponseDto(QuestionsResponseDto questions, List<OptionsResponseDto> options, Long respondentId, String answer, Long optionId, Integer optionSequence) {
        this.questions = questions;
        this.options = options;
        this.respondentId = respondentId;
        this.answer = answer;
        this.optionId = optionId;
        this.optionSequence = optionSequence;
    }
}
