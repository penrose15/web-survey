package com.survey.domain.question.dto;

import com.survey.domain.question.entity.QuestionType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionsResponseDto {
    private Long id;
    private String title;
    private Long surveyId;
    private QuestionType questionType;
}