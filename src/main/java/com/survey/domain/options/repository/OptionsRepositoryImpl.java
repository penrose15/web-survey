package com.survey.domain.options.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.survey.domain.options.dto.FiveMultipleChoiceStatisticDto;
import com.survey.domain.options.entity.QOptions;
import com.survey.domain.question.entity.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.survey.domain.question.entity.QQuestions.questions;
import static com.survey.domain.options.entity.QOptions.options;
import static com.survey.domain.respondent.entity.QRespondent.respondent;

@Repository
@RequiredArgsConstructor
public class OptionsRepositoryImpl implements OptionsRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<FiveMultipleChoiceStatisticDto> findFiveMultipleChoiceStatisticByQuestionIds(Long surveyId) {
        return queryFactory
                .select(Projections.constructor(FiveMultipleChoiceStatisticDto.class,
                        questions.id.as("questionId"),
                        options.id.as("optionId"),
                        options.option.as("option"),
                        options.sequence.as("optionSequence"),
                        respondent.id.count().as("count")))
                .from(options)
                .leftJoin(questions).on(options.questionId.eq(questions.id))
                .leftJoin(respondent).on(options.id.eq(respondent.optionId))
                .where(questions.questionType.eq(QuestionType.FIVE_MULTIPLE_CHOICE)
                        .and(questions.surveyId.eq(surveyId)))
                .groupBy(options.id)
                .fetch();
    }
}
