package com.survey.domain.options.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionsResponseDto {
    private Long id;
    private Integer optionSequence;
    private String choice;

    @Builder
    public OptionsResponseDto(Long id, Integer optionSequence, String choice) {
        this.id = id;
        this.optionSequence = optionSequence;
        this.choice = choice;
    }
}
