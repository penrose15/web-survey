package com.survey.domain.question.dto;

import com.survey.domain.options.dto.OptionCreateDto;
import com.survey.domain.options.dto.OptionUpdateDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionAndOptionUpdateDto {
    private List<QuestionOptionsRequestDto> addQuestionAndOptions;
    private List<QuestionUpdateDto> questionUpdateDtos;
    private List<OptionCreateDto> optionsCreateDtos;
    private List<OptionUpdateDto> optionUpdateDtos;
    private List<Long> deleteQuestionDto;
    private List<Long> deleteOptionsDto;
}
