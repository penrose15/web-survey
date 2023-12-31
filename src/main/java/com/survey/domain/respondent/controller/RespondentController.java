package com.survey.domain.respondent.controller;

import com.survey.domain.respondent.dto.RespondentRequestDto;
import com.survey.domain.respondent.dto.RespondentUpdateDto;
import com.survey.domain.respondent.service.RespondentService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/respondent")
@RequiredArgsConstructor
@RestController
public class RespondentController {
    private final RespondentService respondentService;

    @PostMapping //참가자 답변 생성
    public ResponseEntity<Void> createRespondent(@RequestParam("survey-id")Long surveyId,
                                           @RequestParam("participant-id") Long participantId,
                                           @RequestBody List<RespondentRequestDto> requests) {
        respondentService.createRespondents(requests, surveyId, participantId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /*
    * 선착순 시스템이 아닌 경우에만 참가자가 답변을 수정할 수 있음
    * 유저는 수정 불가능하게 막는 기능 필요
    * */
    @Hidden
    @PatchMapping
    public ResponseEntity<Void> updateRespondent(@RequestParam("survey-id")Long surveyId,
                                                 @RequestParam("participant-id") Long participantId,
                                                 @RequestBody List<RespondentUpdateDto> requests) {
        respondentService.updateRespondent(requests, surveyId, participantId);

        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping // 유저만 삭제 가능
    public ResponseEntity<Void> deleteRespondent(@RequestParam("survey-id")Long surveyId,
                                                 @RequestParam("participant-id") Long participantId) {
        respondentService.deleteRespondent(surveyId, participantId);

        return ResponseEntity.noContent().build();
    }


}
