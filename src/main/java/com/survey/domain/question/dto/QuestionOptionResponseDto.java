package com.survey.domain.question.dto;

import com.survey.domain.options.dto.OptionsResponseDto;
import com.survey.domain.options.entity.Options;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionOptionResponseDto {

    private Long id;
    private String title;
    private Long surveyId;
    private String imageUrl;
    private QuestionType questionType;
    private Integer questionSequence;
    private List<OptionsResponseDto> optionsList;
}
