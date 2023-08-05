package com.survey.domain.question.dto;

import com.survey.domain.options.dto.OptionsRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionUpdateDto {
    private Long questionId;
    private QuestionRequestDto questionRequestDto;
}
