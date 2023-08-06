package com.survey.domain.participant.repository;

import com.survey.domain.participant.dto.ParticipantsResponseDto;
import com.survey.domain.participant.entity.Participants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParticipantsRepository extends JpaRepository<Participants, Long> {

    @Query("SELECT " +
            "com.survey.domain.participant.dto.ParticipantsResponseDto(p.id, p.name, p.email, p.surveyId, p.number) " +
            "FROM Participants p " +
            "WHERE p.id = :id")
    Optional<ParticipantsResponseDto> findByParticipantsId(Long id);

    @Query("SELECT " +
            "new com.survey.domain.participant.dto.ParticipantsResponseDto(p.id, p.name, p.email, p.surveyId, p.number) " +
            "FROM Participants p " +
            "WHERE p.surveyId = :surveyId " +
                "AND p.surveyDone = true ")
    Page<ParticipantsResponseDto> findAllBySurveyIdAndSurveyDoneTrue(Long surveyId, Pageable pageable);
}
