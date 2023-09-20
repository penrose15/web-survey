package com.survey.domain.participant.service;

import com.survey.domain.participant.dto.ParticipantRequestDto;
import com.survey.domain.participant.dto.ParticipantsResponseDto;
import com.survey.domain.participant.entity.Participants;
import com.survey.domain.participant.entity.SurveyStatus;
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
                .status(SurveyStatus.NOT_FINISHED)
                .build();
        participants = participantsRepository.save(participants);
        return participants.getId();
    }

    public Long updateParticipants(Long participantId, ParticipantRequestDto participantRequestDto) {
        Participants participants = getParticipants(participantId);

        participants.updateParticipantInfo(participantRequestDto.getName(), participantRequestDto.getEmail());

        return participants.getId();
    }

    public void updateParticipantsStatus(Long participantId, SurveyStatus status) {
        Participants participants = getParticipants(participantId);
        participants.changeParticipantStatus(status);

        participantsRepository.save(participants);

    }

    public ParticipantsResponseDto findById(Long id) {
        return participantsRepository.findByParticipantsId(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 participants"));
    }

    public Long changeParticipantStatusAndSequence(Long participantId, Integer number, SurveyStatus status) {
        Participants participants = getParticipants(participantId);
        participants.changeParticipantStatus(status);
        participants.setNumber(number);

        participants = participantsRepository.save(participants);

        return participants.getId();
    }

    public Page<ParticipantsResponseDto> findAllBySurveyId(Long surveyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "number");

        return participantsRepository.findAllBySurveyIdAndSurveyDone(surveyId, pageable);
    }


    public Participants getParticipants(Long participantId) {
        return participantsRepository.findById(participantId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 participant"));
    }



    public int participantCount(Long surveyId) {
        return participantsRepository.countParticipantSurveyDone(surveyId);
    }

    public void deleteParticipantsBySurveyId(Long surveyId) {
        participantsRepository.deleteParticipantsBySurveyId(surveyId);
    }
}
