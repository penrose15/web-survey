package com.survey.domain.question.dto;

import com.survey.domain.question.entity.QuestionType;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class QuestionStatisticResponseDto {
    private Long questionId;
    private String title;
    private QuestionType questionType;
    private Integer questionSequence;
    private Long respondentCount;

    @Builder
    public QuestionStatisticResponseDto(Long questionId, String title, QuestionType questionType, Integer questionSequence, Long respondentCount) {
        this.questionId = questionId;
        this.title = title;
        this.questionType = questionType;
        this.questionSequence = questionSequence;
        this.respondentCount = respondentCount;
    }
}
