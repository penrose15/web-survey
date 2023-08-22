package com.survey.domain.participant.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantsResponseDto {
    private Long id;
    private String name;
    private String email;
    private Long surveyId;
    private Integer number;
}
