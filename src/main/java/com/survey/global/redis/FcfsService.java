package com.survey.global.redis;

import com.survey.domain.participant.entity.SurveyStatus;
import com.survey.domain.participant.service.ParticipantsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcfsService {
    private final RedissonClient redissonClient;
    private final UserCountRepository userCountRepository;
    private final ParticipantsService participantsService;

    public void applyFcfsService(final Long surveyId, int userLimit, Long participantId) {
        final RLock lock = redissonClient.getLock("key");

        try {
            if(!lock.tryLock(5, 3, TimeUnit.SECONDS)) {
                log.info("redis getlock timeout");
                return;
            }
            long sequence = userCountRepository.increment(surveyId.toString());
            log.info("sequence = {}",sequence);

            if(sequence > userLimit) {
                participantsService.changeParticipantStatusAndSequence(participantId, (int) sequence, SurveyStatus.NOT_IN_FCFS);
            }
            else {
                participantsService.changeParticipantStatusAndSequence(participantId, (int) sequence, SurveyStatus.IN_FCFS);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
