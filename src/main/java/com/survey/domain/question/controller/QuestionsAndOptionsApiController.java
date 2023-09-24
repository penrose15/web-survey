package com.survey.domain.question.controller;

import com.survey.domain.question.dto.QuestionListRequestDto;
import com.survey.domain.question.dto.QuestionOptionResponseDto;
import com.survey.domain.question.dto.QuestionOptionsRequestDto;
import com.survey.domain.question.service.QuestionAndOptionService;
import com.survey.domain.survey.service.SurveyFindService;
import com.survey.domain.user.entity.User;
import com.survey.global.response.MultiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/question")
@Tag(name = "QuestionsAndOptionsApiController", description = "설문지 작성/수정/조회/삭제")
@RestController
public class QuestionsAndOptionsApiController {
    private final QuestionAndOptionService questionAndOptionService;
    private final SurveyFindService surveyFindService;

    @Operation(description = "create questions and options")
    @PostMapping(value = "/{survey-id}")
    public ResponseEntity<String> createQuestionsAndOptions(@PathVariable("survey-id") Long surveyId,
                                                    @ParameterObject @ModelAttribute QuestionListRequestDto requests,
                                                    @AuthenticationPrincipal User user) {
        verifySurvey(user.getEmail(), surveyId);

        try {
            List<QuestionOptionsRequestDto> request = requests.getQuestionOptionsRequestDtos();
            questionAndOptionService.createQuestionAndOptions(request, surveyId);
            log.info("create questions and options");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }


        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/{survey-id}")
    public ResponseEntity<Void> updateQuestionAndOptions(@PathVariable("survey-id") Long surveyId,
                                                         @ModelAttribute QuestionListRequestDto requests,
                                                         @AuthenticationPrincipal User user) {
        verifySurvey(user.getEmail(), surveyId);
        questionAndOptionService.updateQuestionsAndOptions(surveyId, requests.getQuestionOptionsRequestDtos(), user.getEmail());
        log.info("update question and options");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{survey-id}")
    public ResponseEntity showFormsBySurveyId(@PathVariable("survey-id") Long surveyId,
                                              @RequestParam("page") int page,
                                              @RequestParam("size") int size) {
        Page<QuestionOptionResponseDto> responsePage = questionAndOptionService.getQuestionsAndOptionsBySurveyId(page-1, size, surveyId);
        List<QuestionOptionResponseDto> responseList = responsePage.getContent();

        return ResponseEntity.ok()
                .body(new MultiResponseDto<>(responseList, responsePage));
    }

    @GetMapping("/{survey-id}/{question-id}")
    public ResponseEntity showQuestionAndOptionsById(@PathVariable("survey-id") Long surveyId,
                                                     @PathVariable("question-id") Long id) {
        QuestionOptionResponseDto response = questionAndOptionService.getQuestionAndOptionsByQuestionId(id);

        return ResponseEntity.ok()
                .body(response);
    }

    @DeleteMapping("/{survey-id}/{question-id}")
    public ResponseEntity<Void> deleteQuestionAndOption(@PathVariable("survey-id") Long surveyId,
                                                        @PathVariable("question-id") Long questionId,
                                                        @AuthenticationPrincipal User user){
        verifySurvey(user.getEmail(), surveyId);
        questionAndOptionService.deleteByQuestionId(questionId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{survey-id}")
    public ResponseEntity<Void> deleteQuestionAndOptions(@PathVariable("survey-id") Long surveyId,
                                                         @AuthenticationPrincipal User user) {
        verifySurvey(user.getEmail(), surveyId);
        questionAndOptionService.deleteAllBySurveyId(surveyId);

        return ResponseEntity.ok().build();
    }


    private void verifySurvey(String email, Long surveyId) {
        surveyFindService.findByIdAndEmail(email, surveyId);
    }

}
