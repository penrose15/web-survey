package com.survey.domain.options.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class OptionsResponseDto {
    private Long id;
    private String options;

    @Builder
    public OptionsResponseDto(Long id, String options) {
        this.id = id;
        this.options = options;
    }
}
