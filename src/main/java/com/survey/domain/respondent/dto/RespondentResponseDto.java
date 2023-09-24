package com.survey.domain.respondent.dto;

import com.survey.domain.options.dto.OptionsResponseDto;
import com.survey.domain.question.dto.QuestionsResponseDto;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RespondentResponseDto {
    private QuestionsResponseDto questions;
    private List<OptionsResponseDto> options;
    private Long respondentId;
    private String answer;
    private Long optionIdParticipantChoose;
    private Integer optionSequenceParticipantChoose;

    @Builder
    public RespondentResponseDto(QuestionsResponseDto questions, List<OptionsResponseDto> options, Long respondentId, String answer, Long optionIdParticipantChoose, Integer optionSequenceParticipantChoose) {
        this.questions = questions;
        this.options = options;
        this.respondentId = respondentId;
        this.answer = answer;
        this.optionIdParticipantChoose = optionIdParticipantChoose;
        this.optionSequenceParticipantChoose = optionSequenceParticipantChoose;
    }
}
