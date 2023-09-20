package com.survey.domain.participant.controller;

import com.survey.domain.participant.dto.ParticipantRequestDto;
import com.survey.domain.participant.dto.ParticipantsResponseDto;
import com.survey.domain.participant.service.ParticipantsService;
import com.survey.global.response.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/participant")
@RequiredArgsConstructor
@RestController
public class ParticipantsController {
    private final ParticipantsService participantsService;

    @PostMapping("/{surveyId}")
    public ResponseEntity createParticipants(@PathVariable Long surveyId,
                                             @RequestBody ParticipantRequestDto request) {
        Long participantsId = participantsService.createParticipants(surveyId, request);
        return ResponseEntity.ok()
                .body(participantsId);
    }

    @PatchMapping("/{surveyId}/{participant-id}")
    public ResponseEntity updateParticipants(@PathVariable(value = "surveyId") Long surveyId,
                                             @PathVariable(value = "participant-id") Long id,
                                             @RequestBody ParticipantRequestDto request) {
        Long participantsId = participantsService.updateParticipants(id, request);

        return ResponseEntity.ok().body(participantsId);
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity getParticipantsInfos(@PathVariable(value = "surveyId")Long surveyId,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Page<ParticipantsResponseDto> participantsInfos = participantsService.findAllBySurveyId(surveyId, page, size);
        List<ParticipantsResponseDto> response = participantsInfos.getContent();

        return ResponseEntity
                .ok(new MultiResponseDto<>(response, participantsInfos));
    }


}
