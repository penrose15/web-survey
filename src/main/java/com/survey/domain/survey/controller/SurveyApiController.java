package com.survey.domain.survey.controller;

import com.survey.domain.survey.dto.SurveyCreateDTO;
import com.survey.domain.survey.dto.SurveyResponsesDTO;
import com.survey.domain.survey.dto.SurveyUpdateDTO;
import com.survey.domain.survey.service.SurveyService;
import com.survey.domain.user.entity.User;
import com.survey.global.response.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/survey")
public class SurveyApiController {

    private final SurveyService surveyService;

    @PostMapping
    public Long createSurvey(@AuthenticationPrincipal User user,
                             @RequestBody SurveyCreateDTO request) {
        String email = user.getEmail();

        return surveyService.createSurvey(email, request);
    }

    @PatchMapping("/{id}")
    public Long updateSurvey(@AuthenticationPrincipal User user,
                             @PathVariable Long id,
                             @RequestBody SurveyUpdateDTO request) {
        String email = user.getEmail();
        return surveyService.updateSurvey(id, email, request);

    }

    @GetMapping
    public MultiResponseDto<SurveyResponsesDTO> findUserSurveys(@RequestParam int page,
                                            @RequestParam int size,
                                            @AuthenticationPrincipal User user) {
        Page<SurveyResponsesDTO> responsePage = surveyService.findAll(user.getEmail(), page - 1, size);

        List<SurveyResponsesDTO> responseList = responsePage.getContent();

        return new MultiResponseDto<>(responseList, responsePage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           @AuthenticationPrincipal User user) {
        surveyService.deleteById(user.getEmail(), id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
