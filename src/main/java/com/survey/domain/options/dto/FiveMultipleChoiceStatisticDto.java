package com.survey.domain.options.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class FiveMultipleChoiceStatisticDto {
    private Long questionId;
    private Long optionId;
    private String option;
    private Integer optionSequence;
    private Long count;

    @Builder
    public FiveMultipleChoiceStatisticDto(Long questionId, Long optionId, String option, Integer optionSequence, Long count) {
        this.questionId = questionId;
        this.optionId = optionId;
        this.option = option;
        this.optionSequence = optionSequence;
        this.count = count;
    }
}
