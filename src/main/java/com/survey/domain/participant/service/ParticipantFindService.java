package com.survey.domain.participant.service;

import com.survey.domain.participant.dto.ParticipantsResponseDto;
import com.survey.domain.participant.repository.ParticipantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional
@RequiredArgsConstructor
@Service
public class ParticipantFindService {
    private final ParticipantsRepository participantsRepository;

    public ParticipantsResponseDto findById(Long id) {
        return participantsRepository.findByParticipantsId(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 participants"));
    }


}
