package com.survey.domain.category.service;


import com.survey.domain.category.dto.CategoryRequestDto;
import com.survey.domain.category.entity.Category;
import com.survey.domain.category.repository.CategoryRepository;
import com.survey.domain.participant.service.ParticipantsService;
import com.survey.domain.question.service.QuestionAndOptionService;
import com.survey.domain.respondent.service.RespondentService;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.service.SurveyFindService;
import com.survey.domain.survey.service.SurveyService;
import com.survey.domain.user.entity.User;
import com.survey.domain.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserFindService userFindService;
    private final SurveyFindService surveyFindService;
    private final RespondentService respondentService;
    private final ParticipantsService participantsService;
    private final QuestionAndOptionService questionAndOptionService;
    private final SurveyService surveyService;

    public Long createCategory(CategoryRequestDto request, String email) {
        User user = userFindService.findByEmail(email);

        Category category = Category.builder()
                .category(request.getCategory())
                .userId(user.getId())
                .build();
        category = categoryRepository.save(category);

        return category.getId();
    }

    public Long updateCategory(CategoryRequestDto request, Long categoryId,String email) {
        Category category = getCategory(categoryId);
        User user = userFindService.findByEmail(email);

        if(!Objects.equals(category.getUserId(), user.getId())) throw new IllegalArgumentException("본인의 category에만 접근 가능!");
        category.updateCategory(request.getCategory());

        category = categoryRepository.save(category);

        return category.getId();
    }

    public void deleteCategory(Long categoryId, boolean deleteSurveysInCategory) {
        Category category = getCategory(categoryId);
        if(deleteSurveysInCategory) {
            List<Survey> surveys = surveyFindService.findByCategoryId(categoryId);
            //survey, question, option, participants, respondent 정보 전부 삭제

            for (Survey survey : surveys) {
                respondentService.deleteRespondent(survey.getId());
                participantsService.deleteParticipantsBySurveyId(survey.getId());
                questionAndOptionService.deleteAllBySurveyId(survey.getId());
            }
            surveyService.deleteByCategoryId(categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 category"));
    }




}
