package com.survey.domain.question.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionListRequestDto {
    List<QuestionOptionsRequestDto> questionOptionsRequestDtos;

}
