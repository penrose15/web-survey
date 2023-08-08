package com.survey.domain.options.service;

import com.survey.domain.options.dto.OptionsRequestDto;
import com.survey.domain.options.dto.OptionsResponseDto;
import com.survey.domain.options.entity.Options;
import com.survey.domain.options.repository.OptionRepository;
import com.survey.domain.question.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class OptionsService {
    private final OptionRepository optionRepository;
    private final QuestionService questionService;


    public Long createOption(Long questionId, OptionsRequestDto option) {
        Options options = Options.builder()
                .option(option.getOption())
                .questionId(questionId)
                .build();
        options = optionRepository.save(options);

        return options.getId();
    }

    public void createOptions(Long questionId, List<OptionsRequestDto> optionsRequestDtos) {

        for (OptionsRequestDto option : optionsRequestDtos) {
            Options options = Options.builder()
                    .option(option.getOption())
                    .questionId(questionId)
                    .build();
            optionRepository.save(options);
        }
    }
    public Long updateOptions(Long optionsId, OptionsRequestDto requestDto) {
        Options options = getOptions(optionsId);
        options.updateOption(requestDto);

        optionRepository.save(options);

        return options.getId();
    }

    public void deleteOptions(Long optionsId) {
        Options options = getOptions(optionsId);
        optionRepository.deleteById(optionsId);
    }

    public void deleteOptionsByQuestionId(Long questionId) {
        List<Options> optionsList = findAllByQuestionsId(questionId);

        optionRepository.deleteAll(optionsList);
    }

    public List<Options> findAllByQuestionIds(List<Long> questionIds) {
        return optionRepository.findOptionsList(questionIds);

    }

    public List<Options> findAllByQuestionsId(Long questionId) {

        return optionRepository.findAllByQuestionId(questionId);
    }

    private Options getOptions(Long optionsId) {
        Options options = optionRepository.findById(optionsId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 options"));
        return options;
    }




}
