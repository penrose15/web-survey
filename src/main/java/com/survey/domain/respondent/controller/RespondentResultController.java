package com.survey.domain.respondent.controller;

import com.survey.domain.participant.dto.ParticipantSurveyResponseDto;
import com.survey.domain.respondent.dto.RespondentStatisticFormDto;
import com.survey.domain.respondent.service.RespondentResultService;
import com.survey.domain.survey.service.SurveyFindService;
import com.survey.global.adapter.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/respondent-result")
@RestController
public class RespondentResultController {
    private final RespondentResultService respondentResultService;
    private final SurveyFindService surveyFindService;

    @GetMapping("/{survey-id}/{participant-id}")
    public ResponseEntity<ParticipantSurveyResponseDto> getParticipantRespondent(@PathVariable("survey-id")Long surveyId,
                                                                                 @PathVariable("participant-id")Long participantId,
                                                                                 @RequestParam("page")int page,
                                                                                 @RequestParam("size")int size,
                                                                                 @AuthenticationPrincipal UserAdapter user) {
        String email = user.getUsername();
        surveyFindService.findByIdAndEmail(email, surveyId);

        ParticipantSurveyResponseDto response = respondentResultService.getParticipantsRespondent(participantId, page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistic/{survey-id}")
    public ResponseEntity<RespondentStatisticFormDto> getStatisticBySurveyId(@PathVariable("survey-id")Long surveyId) {
        RespondentStatisticFormDto response = respondentResultService.getStatisticsBySurveyId(surveyId);

        return ResponseEntity.ok(response);
    }


}
