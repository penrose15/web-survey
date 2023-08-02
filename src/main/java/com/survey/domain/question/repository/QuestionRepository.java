package com.survey.domain.question.repository;

import com.survey.domain.question.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Questions, Long> {

}
