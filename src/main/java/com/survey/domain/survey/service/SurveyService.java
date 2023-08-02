package com.survey.domain.survey.service;

import com.survey.domain.survey.dto.SurveyCreateDTO;
import com.survey.domain.survey.dto.SurveyResponsesDTO;
import com.survey.domain.survey.dto.SurveyUpdateDTO;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.repository.SurveyRepository;
import com.survey.domain.user.entity.User;
import com.survey.domain.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final UserFindService userFindService;
    private final SurveyFindService surveyFindService;

    public Long createSurvey(String email, SurveyCreateDTO request) {
        User user = userFindService.findByEmail(email);

        Survey survey = Survey.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startAt(LocalDateTime.parse(request.getStartAt()))
                .endAt(LocalDateTime.parse(request.getEndAt()))
                .userLimit(request.getUserLimit())
                .user(user)
                .categoryId(request.getCategoryId())
                .build();
        survey.validateDateTime(survey.getStartAt(), survey.getEndAt());

        survey = surveyRepository.save(survey);

        log.info("# create survey, id={}",survey.getId());
        return survey.getId();
    }

    public Long updateSurvey(Long surveyId, String email,SurveyUpdateDTO request) {
        Survey survey = surveyFindService.findByIdAndEmail(email, surveyId);

        survey.updateSurvey(request.getTitle(), request.getDescription(), request.getStartAt(), request.getEndAt(), request.getUserLimit(), request.getCategoryId());
        survey = surveyRepository.save(survey);

        log.info("# update survey, id={}", survey.getId());
        return survey.getId();
    }

    public Page<SurveyResponsesDTO> findAll(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Survey> surveyPage = surveyRepository.findByUserEmail(email, pageable);

        log.info("find surveys");
        return surveyPage.map(survey -> SurveyResponsesDTO.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .startAt(survey.getStartAt().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .endAt(survey.getEndAt().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .userLimit(survey.getUserLimit())
                .build());
    }

    //findById는 question 완성 후 생성

    public void deleteById(String email, Long id) {
        Survey survey = surveyFindService.findByIdAndEmail(email, id);

        log.info("# delete survey, id={}", survey.getId());
        surveyRepository.deleteById(survey.getId());
    }

}
