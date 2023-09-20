package com.survey.domain.participant.repository;

import com.survey.domain.participant.dto.ParticipantsResponseDto;
import com.survey.domain.participant.entity.Participants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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
                "AND p.status <> com.survey.domain.participant.entity.SurveyStatus.NOT_FINISHED")
    Page<ParticipantsResponseDto> findAllBySurveyIdAndSurveyDone(Long surveyId, Pageable pageable);

    @Query("select count(p.id) " +
            "from Participants p " +
            "where p.surveyId = :surveyId " +
            "and p.status <> com.survey.domain.participant.entity.SurveyStatus.NOT_FINISHED")
    int countParticipantSurveyDone(Long surveyId);

    @Query("select count(p.id) " +
            "from Participants p " +
            "where p.surveyId = :surveyId " +
            "and p.status = com.survey.domain.participant.entity.SurveyStatus.NOT_IN_FCFS")
    int countParticipantNotInFCFS(Long surveyId);

    @Query("select count(p.id) " +
            "from Participants p " +
            "where p.surveyId = :surveyId " +
            "and p.status = com.survey.domain.participant.entity.SurveyStatus.IN_FCFS")
    int countParticipantInFCFS(Long surveyId);


    @Transactional
    @Modifying
    @Query("delete from Participants p where p.surveyId = :surveyId")
    void deleteParticipantsBySurveyId(Long surveyId);
}
