package com.survey.domain.options.service;

import com.survey.domain.options.dto.OptionsRequestDto;
import com.survey.domain.options.entity.Options;
import com.survey.domain.options.repository.OptionsRepository;
import com.survey.domain.question.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@RequiredArgsConstructor
@Service
public class OptionsService {
    private final OptionsRepository optionsRepository;
    private final QuestionService questionService;


    public Long createOption(Long questionId, OptionsRequestDto option) {
        Options options = Options.builder()
                .option(option.getOption())
                .questionId(questionId)
                .build();
        options = optionsRepository.save(options);

        return options.getId();
    }

    public void createOptions(Long questionId, List<OptionsRequestDto> optionsRequestDtos) {
        int optionSequence = 1;
        for (OptionsRequestDto option : optionsRequestDtos) {
            Options options = Options.builder()
                    .option(option.getOption())
                    .questionId(questionId)
                    .build();
            options.setSequence(optionSequence);
            optionSequence += 1;
            optionsRepository.save(options);
        }
    }
    public Long updateOptions(Long optionsId, OptionsRequestDto requestDto) {
        Options options = getOptions(optionsId);
        options.updateOption(requestDto);

        optionsRepository.save(options);

        return options.getId();
    }

    public void deleteOptions(Long optionsId) {
        Options options = getOptions(optionsId);
        optionsRepository.deleteById(optionsId);
    }

    public void deleteOptionsByQuestionId(Long questionId) {
        List<Options> optionsList = findAllByQuestionsId(questionId);

        optionsRepository.deleteAll(optionsList);
    }

    public List<Options> findAllByQuestionIds(List<Long> questionIds) {
        return optionsRepository.findOptionsList(questionIds);

    }

    public List<Options> findAllByQuestionsId(Long questionId) {

        return optionsRepository.findAllByQuestionId(questionId);
    }

    private Options getOptions(Long optionsId) {
        Options options = optionsRepository.findById(optionsId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 options"));
        return options;
    }




}
