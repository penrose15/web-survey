package com.survey.domain.survey.controller;

import com.survey.domain.survey.dto.SurveyCreateDTO;
import com.survey.domain.survey.dto.SurveyResponsesDTO;
import com.survey.domain.survey.dto.SurveyUpdateDTO;
import com.survey.domain.survey.service.SurveyService;
import com.survey.domain.user.entity.User;
import com.survey.global.adapter.UserAdapter;
import com.survey.global.response.MultiResponseDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
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
    public ResponseEntity<Long> createSurvey(@AuthenticationPrincipal UserAdapter user,
                             @RequestBody SurveyCreateDTO request) {
        String email = user.getUsername();

        Long surveyId = surveyService.createSurvey(email, request);

        return ResponseEntity.ok(surveyId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> updateSurvey(@AuthenticationPrincipal UserAdapter user,
                             @PathVariable("id") Long id,
                             @RequestBody SurveyUpdateDTO request) {
        String email = user.getUsername();
        Long surveyId =  surveyService.updateSurvey(id, email, request);
        return ResponseEntity.ok(surveyId);
    }

    @GetMapping
    public MultiResponseDto<SurveyResponsesDTO> findUserSurveys(@RequestParam int page,
                                            @RequestParam int size,
                                            @AuthenticationPrincipal UserAdapter user) {
        Page<SurveyResponsesDTO> responsePage = surveyService.findAll(user.getUsername(), page - 1, size);

        List<SurveyResponsesDTO> responseList = responsePage.getContent();

        return new MultiResponseDto<>(responseList, responsePage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           @AuthenticationPrincipal UserAdapter user) {
        surveyService.deleteById(user.getUsername(), id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
