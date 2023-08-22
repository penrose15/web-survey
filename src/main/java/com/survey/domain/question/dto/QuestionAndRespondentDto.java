package com.survey.domain.question.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionAndRespondentDto {
    private Long questionId;
    private String questionTitle;
    private Integer questionSequence;
    private Long respondentId;
    private Long respondentOptionId;
    @Builder.Default
    private Integer respondentOptionSequence = null;
    @Builder.Default
    private String answer = "";

}
