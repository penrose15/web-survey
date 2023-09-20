package com.survey.domain.survey.repository;

import com.survey.domain.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    @Query("select s " +
            "from Survey s " +
            "join User u " +
            "on s.user.id = u.id " +
            "where u.email = :email")
    Page<Survey> findByUserEmail(String email, Pageable pageable);


    @Query("select s " +
            "from Survey s " +
            "where s.categoryId = :categoryId")
    List<Survey> findSurveysByCategoryId(Long categoryId);

    @Transactional
    @Modifying
    @Query("delete from Survey s where s.categoryId = :categoryId")
    void deleteByCategoryId(Long categoryId);
}
