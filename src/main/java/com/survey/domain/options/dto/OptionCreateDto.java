package com.survey.domain.options.dto;

import lombok.Getter;

@Getter
public class OptionCreateDto {
    private Long questionId;
    private OptionsRequestDto optionsRequestDto;
}
