package com.survey.domain.respondent.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RespondentEssayDto {
    private Long questionId;
    private String answer;

    @Builder
    public RespondentEssayDto(Long questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }
}
