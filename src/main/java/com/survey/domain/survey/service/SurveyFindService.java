package com.survey.domain.survey.service;

import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SurveyFindService {
    private final SurveyRepository surveyRepository;
    public Survey findByIdAndEmail(String email, Long id) {
        Survey survey =  findSurveyById(id);
        if(!Objects.equals(survey.getUser().getEmail(), email)) {
            throw new IllegalStateException("잘못된 접근");
        }
        return survey;
    }

    public Survey findSurveyById(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 설문조사"));
    }
    public List<Survey> findByCategoryId(Long categoryId) {
        return surveyRepository.findSurveysByCategoryId(categoryId);
    }

}
