package com.survey.domain.question.dto;

import com.survey.domain.options.dto.OptionsRequestDto;
import com.survey.domain.question.dto.QuestionRequestDto;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionOptionsRequestDto {
    private QuestionRequestDto question;
    private List<OptionsRequestDto> options;

    public QuestionOptionsRequestDto(QuestionRequestDto question, List<OptionsRequestDto> options) {
        this.question = question;
        this.options = options;
    }
}
