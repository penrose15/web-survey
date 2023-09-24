package com.survey.domain.options.service;

import com.survey.domain.options.dto.OptionsRequestDto;
import com.survey.domain.options.entity.Options;
import com.survey.domain.options.repository.OptionsRepository;
import com.survey.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OptionsService {
    private final OptionsRepository optionsRepository;
    private final QuestionService questionService;


    public Long createOption(Long questionId, OptionsRequestDto option) {
        Options options = Options.builder()
                .choice(option.getChoice())
                .questionId(questionId)
                .build();
        options = optionsRepository.save(options);

        return options.getId();
    }

    public void createOptions(Long questionId, List<OptionsRequestDto> optionsRequestDtos) {
        int optionSequence = 1;
        log.info("optionsList size = {}", optionsRequestDtos.size());
        List<Options> options = new ArrayList<>();
        for (OptionsRequestDto request : optionsRequestDtos) {
            log.info("options choice = {}", request.getChoice());
            Options option = Options.builder()
                    .choice(request.getChoice())
                    .questionId(questionId)
                    .sequence(optionSequence)
                    .build();
            optionSequence += 1;
            options.add(option);
        }
        optionsRepository.saveAll(options);
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

    public void deleteOptionsByQuestionIds(List<Long> questionIds) {
        optionsRepository.deleteByQuestionIds(questionIds);
    }

    @Transactional(readOnly = true)
    public List<Options> findAllByQuestionIds(List<Long> questionIds) {
        return optionsRepository.findOptionsList(questionIds);

    }

    @Transactional(readOnly = true)
    public List<Options> findAllByQuestionsId(Long questionId) {

        return optionsRepository.findAllByQuestionId(questionId);
    }

    @Transactional(readOnly = true)
    private Options getOptions(Long optionsId) {
        Options options = optionsRepository.findById(optionsId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 options"));
        return options;
    }




}
