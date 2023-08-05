package com.survey.domain.options.dto;

import com.survey.domain.question.dto.QuestionUpdateDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionAndOptionUpdateDto {
    private List<QuestionOptionsRequestDto> addQuestionAndOptions;
    private List<QuestionUpdateDto> questionUpdateDtos;
    private List<OptionCreateDto> optionsCreateDtos;
    private List<OptionUpdateDto> optionUpdateDtos;
    private List<Long> deleteQuestionDto;
    private List<Long> deleteOptionsDto;
}
