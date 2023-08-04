package com.survey.domain.options.dto;

import com.survey.domain.question.dto.QuestionRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionOptionsRequestDto {
    private QuestionRequestDto question;
    private List<OptionsRequestDto> options;

    public QuestionOptionsRequestDto(QuestionRequestDto question, List<OptionsRequestDto> options) {
        this.question = question;
        if(this.options == null) {
            this.options = new ArrayList<>();
        }
    }
}
