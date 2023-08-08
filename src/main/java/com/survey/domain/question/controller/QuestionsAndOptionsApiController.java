package com.survey.domain.question.controller;

import com.survey.domain.question.dto.QuestionAndOptionUpdateDto;
import com.survey.domain.question.dto.QuestionOptionResponseDto;
import com.survey.domain.question.dto.QuestionOptionsRequestDto;
import com.survey.domain.question.service.QuestionAndOptionService;
import com.survey.domain.question.service.QuestionService;
import com.survey.domain.survey.service.SurveyFindService;
import com.survey.domain.user.entity.User;
import com.survey.global.response.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/survey")
@RestController
public class QuestionsAndOptionsApiController {
    private final QuestionService questionService;
    private final QuestionAndOptionService questionAndOptionService;
    private final SurveyFindService surveyFindService;

    @PostMapping("/{survey-id}/question")
    public ResponseEntity<Void> createQuestionsAndOptions(@PathVariable("survey-id") Long surveyId,
                                                    @RequestBody List<QuestionOptionsRequestDto> requests,
                                                    @AuthenticationPrincipal User user) {
        verifySurvey(user.getEmail(), surveyId);

        questionAndOptionService.createQuestionAndOptions(requests, surveyId, user.getEmail());
        log.info("create questions and options");
        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/{survey-id}/question/correction")
    public ResponseEntity<Void> updateQuestionAndOptions(@PathVariable("survey-id") Long surveyId,
                                                         @RequestBody QuestionAndOptionUpdateDto request,
                                                         @AuthenticationPrincipal User user) {
        verifySurvey(user.getEmail(), surveyId);
        questionAndOptionService.updateQuestionsAndOptions(surveyId, request, user.getEmail());
        log.info("update question and options");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{survey-id}/forms")
    public ResponseEntity showFormsBySurveyId(@PathVariable("survey-id") Long surveyId,
                                              @RequestParam("page") int page,
                                              @RequestParam("size") int size) {
        Page<QuestionOptionResponseDto> responsePage = questionAndOptionService.getQuestionsAndOptionsBySurveyId(page, size, surveyId);
        List<QuestionOptionResponseDto> responseList = responsePage.getContent();

        return ResponseEntity.ok()
                .body(new MultiResponseDto<>(responseList, responsePage));
    }

    @GetMapping("/{survey-id}/forms/{question-id}")
    public ResponseEntity showQuestionAndOptionsById(@PathVariable("survey-id") Long surveyId,
                                                     @PathVariable("question-id") Long id) {
        QuestionOptionResponseDto response = questionAndOptionService.getQuestionAndOptionsByQuestionId(id);

        return ResponseEntity.ok()
                .body(response);
    }

    @DeleteMapping("/{survey-id}/question/clear")
    public ResponseEntity<Void> deleteQuestionAndOptions(@PathVariable("survey-id") Long surveyId,
                                                         @RequestBody QuestionAndOptionUpdateDto request,
                                                         @AuthenticationPrincipal User user) {
        verifySurvey(user.getEmail(), surveyId);
        questionAndOptionService.deleteAllBySurveyId(surveyId);

        return ResponseEntity.ok().build();
    }


    private void verifySurvey(String email, Long surveyId) {
        surveyFindService.findByIdAndEmail(email, surveyId);
    }

}
