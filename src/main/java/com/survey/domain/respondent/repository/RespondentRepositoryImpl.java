package com.survey.domain.respondent.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.respondent.dto.RespondentEssayDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.survey.domain.question.entity.QQuestions.questions;
import static com.survey.domain.respondent.entity.QRespondent.respondent;

@Repository
@RequiredArgsConstructor
public class RespondentRepositoryImpl implements RespondentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RespondentEssayDto> findAnswerByTypeIsEssayAndSurveyId(Long surveyId) {
        return queryFactory
                .select(Projections.constructor(RespondentEssayDto.class,
                        questions.id.as("questionId"),
                        respondent.answer.as("answer")))
                .from(respondent)
                .join(questions).on(questions.id.eq(respondent.questionId))
                .where(questions.questionType.eq(QuestionType.ESSAY)
                        .and(respondent.surveyId.eq(surveyId)))
                .fetch();
    }
}
