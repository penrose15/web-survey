package com.survey.domain.options.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionsResponseDto {
    private Long id;
    private Integer optionSequence;
    private String options;

    @Builder
    public OptionsResponseDto(Long id, Integer optionSequence, String options) {
        this.id = id;
        this.optionSequence = optionSequence;
        this.options = options;
    }
}
