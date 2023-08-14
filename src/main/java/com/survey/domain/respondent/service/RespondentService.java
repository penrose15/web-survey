package com.survey.domain.respondent.service;

import com.survey.domain.participant.entity.Participants;
import com.survey.domain.participant.service.ParticipantsService;
import com.survey.domain.question.service.QuestionService;
import com.survey.domain.respondent.dto.RespondentRequestDto;
import com.survey.domain.respondent.entity.Respondent;
import com.survey.domain.respondent.repository.RespondentJdbcRepository;
import com.survey.domain.respondent.repository.RespondentRepository;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.service.SurveyFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class RespondentService {

    private final RespondentRepository respondentRepository;
    private final RespondentJdbcRepository respondentJdbcRepository;
    private final QuestionService questionService;
    private final SurveyFindService surveyFindService;
    private final ParticipantsService participantsService;

    public void createRespondents(List<RespondentRequestDto> requests, Long surveyId, Long participantId) {
        Survey survey = surveyFindService.findSurveyById(surveyId);
        int count = questionService.countQuestionsBySurveyId(surveyId);

        if(count != requests.size()) throw new IllegalStateException("모든 질문에 답변해주세요");

        List<Respondent> respondents = new ArrayList<>();
        for (RespondentRequestDto request : requests) {
            //서술형인 경우 options가 null이므로
            Long optionId = request.getOptionId() != null ? request.getOptionId() : null;

            Respondent respondent = Respondent.builder()
                    .answer(request.getAnswer())
                    .optionId(optionId)
                    .participantsId(participantId)
                    .surveyId(surveyId)
                    .build();
            respondents.add(respondent);
        }

        //보통의 설문조사라면 이대로 저장하면 되나...
        //선착순 서비스가 있으므로 여기서부턴 좀 복잡해질듯하다...

        // userLimit를 정하지 않았다면 그냥 저장
        if(survey.getUserLimit() != -1) {
            /**
             * survey에 이미 참가한 participants 수를 센다.
             * 만약 participants 의 숫자가 survey의 userlimit 보다 적다면
             * respondents를 저장하고 participant의 surveyDone을 true로 변경, number를 survey에 참여한 참여자 수 + 1로 저장한다 (아니면 redis로 +1 씩 한 숫자로 설정하든가)
             *
             * 만약 userlimit보다 크다면..?
             * 이야기를 해봐야 할 듯하다.
             */
        }
        else {
            respondentJdbcRepository.saveAll(respondents);
            participantsService.changeParticipantStatus(participantId);
        }
    }

    
}
