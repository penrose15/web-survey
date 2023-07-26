package com.survey.domain.survey.repository;

import com.survey.domain.survey.dto.SurveyResponsesDTO;
import com.survey.domain.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    @Query("select s " +
            "from Survey s " +
            "join User u " +
            "on s.user.id = u.id " +
            "where u.email = :email " +
            "and s.id = :id")
    Optional<Survey> findByIdAndUserEmail(Long id, String email);

    @Query("select s " +
            "from Survey s " +
            "join User u " +
            "on s.user.id = u.id " +
            "where u.email = :email")
    Page<Survey> findByUserEmail(String email, Pageable pageable);
}
