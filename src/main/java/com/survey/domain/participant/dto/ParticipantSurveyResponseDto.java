package com.survey.domain.participant.dto;

import com.survey.domain.participant.entity.SurveyStatus;
import com.survey.domain.respondent.dto.RespondentResponseDto;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantSurveyResponseDto {
    private Long participantId;
    private Long surveyId;
    private String name;
    private String email;
    private SurveyStatus participantStatus;
    private List<RespondentResponseDto> respondents;

    @Builder
    public ParticipantSurveyResponseDto(Long participantId, Long surveyId, String name, String email, SurveyStatus participantStatus, List<RespondentResponseDto> respondents) {
        this.participantId = participantId;
        this.surveyId = surveyId;
        this.name = name;
        this.email = email;
        this.participantStatus = participantStatus;
        this.respondents = respondents;
    }
}
