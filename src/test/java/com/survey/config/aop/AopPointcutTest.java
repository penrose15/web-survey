package com.survey.config.aop;

import com.survey.domain.respondent.dto.RespondentRequestDto;
import com.survey.domain.respondent.service.RespondentService;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AopPointcutTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method method;

    @Test
    void testPointcut() throws NoSuchMethodException {
        Class<?>[] parameters = new Class[]{List.class, Long.class, Long.class};
        method = RespondentService.class.getMethod("createRespondents",parameters);

        pointcut.setExpression("execution(* com.survey..*.*(..))");

        assertThat(pointcut.matches(method, RespondentService.class))
                .isTrue();
    }
}
