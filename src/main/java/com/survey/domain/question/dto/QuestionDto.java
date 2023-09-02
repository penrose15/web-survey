package com.survey.domain.question.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {
    @NotBlank
    private String title;

    private boolean essential;

    @NotBlank
    private String questionType;
}
