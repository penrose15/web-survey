package com.survey.global.aop;

import com.survey.domain.survey.service.SurveyFindService;
import com.survey.global.adapter.UserAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class UserCheckAop {
    private final SurveyFindService surveyFindService;

    @Around("execution(* com.survey.domain.respondent.controller.RespondentController.deleteRespondent(..))")
    public Object checkSurveyIsFromUser(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("# start check User");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAdapter user = (UserAdapter) authentication.getPrincipal();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String surveyIdParameter = request.getParameter("survey-id");
        Long surveyId = Long.parseLong(surveyIdParameter);

        surveyFindService.findByIdAndEmail(user.getUsername(), surveyId);

        return joinPoint.proceed();
    }


    @Pointcut("execution(* com.survey.domain..*.*(..))")
    private void log(){};

    @Before(value = "log()")
    public void beforeMethodLog(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.info("===== method name = {} =====", method.getName());

        Object[] args = joinPoint.getArgs();
        if(args.length <= 0) log.info("no parameter");
        for (Object arg : args) {
            if(arg == null) continue;
            log.info("parameter type = {}", arg.getClass().getSimpleName());
            log.info("parameter value = {}", arg);
        }
    }

    @AfterReturning(value = "log()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        Method method = getMethod(joinPoint);
        log.info("======= method name = {} =======", method.getName());

        if(returnObj.getClass() != null) {
            log.info("return type = {}", returnObj.getClass().getSimpleName());
        }
        else log.info("void method");
        log.info("return value = {}", returnObj);
    }

    @AfterThrowing(value = "execution(* com.survey..*.*(..))", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        Method method = getMethod(joinPoint);
        log.info("======= method name = {} =======", method.getName());
        log.info("exception = {}", ex.getMessage());
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
