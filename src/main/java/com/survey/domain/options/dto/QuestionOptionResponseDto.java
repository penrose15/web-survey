package com.survey.domain.options.dto;

import com.survey.domain.options.entity.Options;
import com.survey.domain.question.entity.Questions;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionOptionResponseDto {

    private Questions questions;
    private List<Options> optionsList;

    @Builder
    public QuestionOptionResponseDto(Questions questions, List<Options> optionsList) {
        this.questions = questions;
        this.optionsList = optionsList;
    }
}
