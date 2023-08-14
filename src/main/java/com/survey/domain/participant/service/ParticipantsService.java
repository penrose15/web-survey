package com.survey.domain.participant.service;

import com.survey.domain.participant.dto.ParticipantRequestDto;
import com.survey.domain.participant.dto.ParticipantsResponseDto;
import com.survey.domain.participant.entity.Participants;
import com.survey.domain.participant.repository.ParticipantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional
@RequiredArgsConstructor
@Service
public class ParticipantsService {
    private final ParticipantsRepository participantsRepository;

    public Long createParticipants(Long surveyId, ParticipantRequestDto participantInfo) {
        Participants participants = Participants.builder()
                .name(participantInfo.getName())
                .email(participantInfo.getEmail())
                .surveyId(surveyId)
                .build();
        participants = participantsRepository.save(participants);
        return participants.getId(); //저장된 participant 의 Id 는 쿠키에 저장한다.
    }

    //쿠키에 저장한 participantId로 조회한다.
    public Long updateParticipants(Long participantId, ParticipantRequestDto participantRequestDto) {
        Participants participants = getParticipants(participantId);

        participants.updateParticipantInfo(participantRequestDto.getName(), participantRequestDto.getEmail());

        return participants.getId();
    }

    public ParticipantsResponseDto findById(Long id) {
        return participantsRepository.findByParticipantsId(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 participants"));
    }

    public Long changeParticipantStatus(Long participantId) {
        Participants participants = getParticipants(participantId);
        participants.checkParticipantSurveyHasDone();
        participants.changeParticipantStatus();

        participantsRepository.save(participants);

        return participants.getId();
    }

    public Page<ParticipantsResponseDto> findAllBySurveyId(Long surveyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "number");

        return participantsRepository.findAllBySurveyIdAndSurveyDoneTrue(surveyId, pageable);
    }

    private Participants getParticipants(Long participantId) {
        return participantsRepository.findById(participantId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 participant"));
    }
}
