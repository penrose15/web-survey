package com.survey.domain.respondent.service;

import com.survey.domain.options.dto.FiveMultipleChoiceStatisticDto;
import com.survey.domain.options.dto.OptionsResponseDto;
import com.survey.domain.options.repository.OptionsRepositoryImpl;
import com.survey.domain.participant.dto.ParticipantSurveyResponseDto;
import com.survey.domain.participant.entity.Participants;
import com.survey.domain.participant.repository.ParticipantsRepository;
import com.survey.domain.question.dto.QuestionAndOptionStatisticDto;
import com.survey.domain.question.dto.QuestionOptionResponseDto;
import com.survey.domain.question.dto.QuestionStatisticResponseDto;
import com.survey.domain.question.dto.QuestionsResponseDto;
import com.survey.domain.question.repository.QuestionsRepositoryImpl;
import com.survey.domain.question.service.QuestionAndOptionService;
import com.survey.domain.respondent.dto.RespondentEssayDto;
import com.survey.domain.respondent.dto.RespondentResponseDto;
import com.survey.domain.respondent.dto.RespondentStatisticFormDto;
import com.survey.domain.respondent.entity.Respondent;
import com.survey.domain.respondent.repository.RespondentRepository;
import com.survey.domain.respondent.repository.RespondentRepositoryImpl;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RespondentResultService {
    private final ParticipantsRepository participantsRepository;
    private final RespondentRepository respondentRepository;
    private final QuestionAndOptionService questionAndOptionService;
    private final QuestionsRepositoryImpl questionsRepositoryImpl;
    private final OptionsRepositoryImpl optionsRepositoryImpl;
    private final RespondentRepositoryImpl respondentRepositoryImpl;
    private final SurveyRepository surveyRepository;

    //참여자 개인의 response
    public ParticipantSurveyResponseDto getParticipantsRespondent(Long participantsId, int page, int size) {
        Participants participants = participantsRepository.findById(participantsId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 participant"));
        Long surveyId = participants.getSurveyId();
        List<Respondent> respondents = respondentRepository.findListByParticipantsId(participantsId);

        Page<QuestionOptionResponseDto> questionAndOptionsPage = questionAndOptionService.getQuestionsAndOptionsBySurveyId(page, size, surveyId);
        List<QuestionOptionResponseDto> questionAndOptions = questionAndOptionsPage.getContent();

        Map<Long, Respondent> respondentMap = respondents.stream()
                .collect(Collectors.toMap(
                        Respondent::getQuestionId,
                        i2 -> i2
                ));

        List<RespondentResponseDto> responses = getResponses(questionAndOptions, respondentMap);
        log.info("#################################");
        for (RespondentResponseDto response : responses) {
            System.out.println(response);
        }

        return ParticipantSurveyResponseDto.builder()
                .participantId(participantsId)
                .surveyId(participants.getSurveyId())
                .name(participants.getName())
                .email(participants.getEmail())
                .participantStatus(participants.getStatus())
                .respondents(responses)
                .build();

    }
    //통계
    public RespondentStatisticFormDto getStatisticsBySurveyId(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 survey"));

        return RespondentStatisticFormDto.builder()
                .surveyId(survey.getId())
                .surveyName(survey.getTitle())
                .startAt((survey.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .endAt(survey.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .userLimit(survey.getUserLimit())
                .statistics(getQuestionAndOptionStatisticBySurvey(survey.getId()))
                .build();
    }


    public List<QuestionAndOptionStatisticDto> getQuestionAndOptionStatisticBySurvey(Long surveyId) {
        surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 survey"));

        List<QuestionStatisticResponseDto> fiveMultipleChoiceQuestionStatistics = questionsRepositoryImpl.findQuestionStatisticsByTypeIsFiveMultipleChoice(surveyId);
        List<QuestionStatisticResponseDto> essayQuestionStatistics = questionsRepositoryImpl.findQuestionStatisticsByTypeIsEssay(surveyId);

        List<FiveMultipleChoiceStatisticDto> fiveMultipleChoiceStatistics = optionsRepositoryImpl.findFiveMultipleChoiceStatisticByQuestionIds(surveyId);
        List<RespondentEssayDto> essays = respondentRepositoryImpl.findAnswerByTypeIsEssayAndSurveyId(surveyId);

        Map<Long, List<FiveMultipleChoiceStatisticDto>> fiveMultipleChoiceStatisticMap =
                getFiveMultipleChoiceStatisticsMap(fiveMultipleChoiceStatistics);
        Map<Long, List<String>> essayMap = essays.stream()
                .collect(Collectors.groupingBy(RespondentEssayDto::getQuestionId,
                        Collectors.mapping(RespondentEssayDto::getAnswer, Collectors.toList())));


        List<QuestionAndOptionStatisticDto> fiveMultipleChoiceStatisticList =  getQuestionAndOptionStatisticDtos(fiveMultipleChoiceQuestionStatistics, fiveMultipleChoiceStatisticMap, null);
        List<QuestionAndOptionStatisticDto> essayStatisticList =  getQuestionAndOptionStatisticDtos(essayQuestionStatistics, null, essayMap);

        return getQuestionAndOptionStatisticDtos(fiveMultipleChoiceStatisticList, essayStatisticList);
    }

    private List<QuestionAndOptionStatisticDto> getQuestionAndOptionStatisticDtos(List<QuestionAndOptionStatisticDto> fiveMultipleChoiceStatisticList, List<QuestionAndOptionStatisticDto> essayStatisticList) {
        PriorityQueue<QuestionAndOptionStatisticDto> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.getQuestion().getQuestionSequence()));
        priorityQueue.addAll(fiveMultipleChoiceStatisticList);
        priorityQueue.addAll(essayStatisticList);


        List<QuestionAndOptionStatisticDto> result = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            result.add(priorityQueue.poll());
        }
        return result;
    }

    private List<QuestionAndOptionStatisticDto> getQuestionAndOptionStatisticDtos(List<QuestionStatisticResponseDto> questionStatistics, Map<Long, List<FiveMultipleChoiceStatisticDto>> fiveMultipleChoiceStatisticMap, Map<Long, List<String>> essayMap) {
        log.info("essayMap is null ? : {}",essayMap == null);

        if(fiveMultipleChoiceStatisticMap != null) {
            return questionStatistics
                    .stream()
                    .map(q -> QuestionAndOptionStatisticDto.builder()
                            .question(q)
                            .fiveMultipleChoiceStatistics(fiveMultipleChoiceStatisticMap.get(q.getQuestionId()))
                            .build()).toList();
        }
        else {
            return questionStatistics
                    .stream()
                    .map(q -> QuestionAndOptionStatisticDto.builder()
                            .question(q)
                            .essayStatistics(essayMap.get(q.getQuestionId()))
                            .build()).toList();
        }
    }

    private Map<Long, QuestionStatisticResponseDto> getQuestionStatisticResponseDtoMap(List<QuestionStatisticResponseDto> questionStatistics) {
        return questionStatistics.stream()
                .collect(Collectors.toMap(
                        QuestionStatisticResponseDto::getQuestionId,
                        i2 -> i2
                ));
    }

    private Map<Long, List<FiveMultipleChoiceStatisticDto>> getFiveMultipleChoiceStatisticsMap(List<FiveMultipleChoiceStatisticDto> fiveMultipleChoiceStatistics) {
        return fiveMultipleChoiceStatistics
                .stream()
                .collect(Collectors.groupingBy(FiveMultipleChoiceStatisticDto::getQuestionId));
    }

    private List<RespondentResponseDto> getResponses(List<QuestionOptionResponseDto> questionsAndOptions, Map<Long, Respondent> respondentMap) {
        return questionsAndOptions.stream()
                .map(question -> {
                    Long respondentId = null;
                    String answer = null;
                    Long optionIdParticipantChoose = null;
                    Integer optionSequenceParticipantChoose = null;
                    Respondent respondent = respondentMap.getOrDefault(question.getQuestionId(), null);
                    if (respondent != null) {
                        respondentId = respondent.getId();
                        answer = respondent.getAnswer();
                        optionIdParticipantChoose = respondent.getOptionId();
                        optionSequenceParticipantChoose = respondent.getOptionSequence();
                    }

                    return RespondentResponseDto.builder()
                            .questions(getQuestions(question))
                            .options(getOptions(question))
                            .respondentId(respondentId)
                            .answer(answer)
                            .optionIdParticipantChoose(optionIdParticipantChoose)
                            .optionSequenceParticipantChoose(optionSequenceParticipantChoose)
                            .build();
                }).toList();
    }


    private QuestionsResponseDto getQuestions(QuestionOptionResponseDto questionOption) {
        return QuestionsResponseDto.builder()
                .id(questionOption.getQuestionId())
                .title(questionOption.getTitle())
                .surveyId(questionOption.getSurveyId())
                .questionType(questionOption.getQuestionType().name())
                .build();
    }

    private List<OptionsResponseDto> getOptions(QuestionOptionResponseDto questionOption) {
        return questionOption.getOptionsList()
                .stream()
                .map(o -> OptionsResponseDto.builder()
                        .id(o.getId())
                        .optionSequence(o.getOptionSequence())
                        .choice(o.getChoice())
                        .build()).collect(Collectors.toList());
    }
}
