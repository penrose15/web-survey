package com.survey.domain.options.repository;

import com.survey.domain.options.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Options, Long> {
}
