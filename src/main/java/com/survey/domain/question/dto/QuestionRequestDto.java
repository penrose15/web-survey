package com.survey.domain.question.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class QuestionRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String questionType;

}
