package com.survey.domain.question.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.survey.domain.question.dto.QuestionStatisticResponseDto;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.list;
import static com.querydsl.core.types.dsl.Expressions.as;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.survey.domain.question.entity.QQuestions.questions;
import static com.survey.domain.respondent.entity.QRespondent.respondent;

@Repository
@RequiredArgsConstructor
public class QuestionsRepositoryImpl implements QuestionsRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<QuestionStatisticResponseDto> findQuestionStatisticsByTypeIsFiveMultipleChoice(Long surveyId) {
        return queryFactory
                .select(Projections.constructor(QuestionStatisticResponseDto.class,
                        questions.id.as("questionId"),
                        questions.title.as("title"),
                        questions.questionType.as("questionType"),
                        questions.sequence.as("questionSequence"),
                        respondent.id.count().as("respondentCount")))
                .from(questions)
                .leftJoin(respondent).on(questions.id.eq(respondent.questionId))
                .where(questions.questionType.eq(QuestionType.FIVE_MULTIPLE_CHOICE)
                        .and(questions.surveyId.eq(surveyId)))
                .groupBy(questions.id)
                .fetch();
    }

    @Override
    public List<QuestionStatisticResponseDto> findQuestionStatisticsByTypeIsEssay(Long surveyId) {
        return queryFactory
                .select(Projections.constructor(QuestionStatisticResponseDto.class,
                        questions.id.as("questionId"),
                        questions.title.as("title"),
                        questions.questionType.as("questionType"),
                        questions.sequence.as("questionSequence"),
                        respondent.id.count().as("respondentCount")))
                .from(questions)
                .leftJoin(respondent).on(questions.id.eq(respondent.questionId))
                .where(questions.questionType.eq(QuestionType.ESSAY)
                        .and(questions.surveyId.eq(surveyId)))
                .groupBy(questions.id)
                .fetch();
    }

    @Override
    public List<Questions> findBySurveyAndQuestionTypeIsFiveMultipleChoice(Long surveyId) {
        return queryFactory
                .selectFrom(questions)
                .where(questions.surveyId.eq(surveyId)
                        .and(questions.questionType.eq(QuestionType.FIVE_MULTIPLE_CHOICE)))
                .orderBy(questions.sequence.asc())
                .fetch();
    }

    @Override
    public List<Questions> findBySurveyAndQuestionTypeIsEssay(Long surveyId) {
        return queryFactory
                .selectFrom(questions)
                .where(questions.surveyId.eq(surveyId)
                        .and(questions.questionType.eq(QuestionType.ESSAY)))
                .orderBy(questions.sequence.asc())
                .fetch();
    }
}
