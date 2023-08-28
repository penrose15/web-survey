package com.survey.domain.participant.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.survey.domain.options.entity.Options;
import com.survey.domain.participant.dto.ParticipantSurveyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.survey.domain.participant.entity.QParticipants.participants;

@Repository
@RequiredArgsConstructor
public class ParticipantsRepositoryImpl implements ParticipantsRepositoryCustom{
    private final JPAQueryFactory queryFactory;


}
