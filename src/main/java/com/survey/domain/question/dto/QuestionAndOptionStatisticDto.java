package com.survey.domain.question.dto;

import com.survey.domain.options.dto.FiveMultipleChoiceStatisticDto;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionAndOptionStatisticDto {
    private QuestionStatisticResponseDto question;
    private List<FiveMultipleChoiceStatisticDto> fiveMultipleChoiceStatistics;
    private List<String> essayStatistics;

    @Builder
    public QuestionAndOptionStatisticDto(QuestionStatisticResponseDto question, List<FiveMultipleChoiceStatisticDto> fiveMultipleChoiceStatistics, List<String> essayStatistics) {
        this.question = question;
        this.fiveMultipleChoiceStatistics = fiveMultipleChoiceStatistics;
        this.essayStatistics = essayStatistics;
    }
}
