package com.survey.domain.respondent.controller;

import com.survey.domain.participant.dto.ParticipantSurveyResponseDto;
import com.survey.domain.respondent.dto.RespondentStatisticFormDto;
import com.survey.domain.respondent.service.RespondentResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/respondent-result")
@RestController
public class RespondentResultController {
    private final RespondentResultService respondentResultService;

    @GetMapping("/{participant-id}")
    public ResponseEntity<ParticipantSurveyResponseDto> getParticipantRespondent(@PathVariable("participant-id")Long participantId,
                                                                                 @RequestParam("page")int page,
                                                                                 @RequestParam("size")int size) {
        ParticipantSurveyResponseDto response = respondentResultService.getParticipantsRespondent(participantId, page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistic/{survey-id}")
    public ResponseEntity<RespondentStatisticFormDto> getStatisticBySurveyId(@PathVariable("survey-id")Long surveyId) {
        RespondentStatisticFormDto response = respondentResultService.getStatisticsBySurveyId(surveyId);

        return ResponseEntity.ok(response);
    }


}
