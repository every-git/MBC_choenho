package org.zerock.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import java.util.Arrays;

@Component
@Aspect
@Log4j2
public class LogAdvice {

    @Before("execution(* org.zerock.service.*.*(..))")
    public void logParams(JoinPoint jp) {
        log.info("========== logParams ==========");

        Object[] params = jp.getArgs();
        log.info(Arrays.toString(params));
        Object target = jp.getTarget();
        log.info("target: " + target);

        log.info("========== logParams end ==========");
    }

    @Around("execution(* org.zerock.service.*.*(..))")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        log.info("========== logAround ==========");
        
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();
        log.info("time: " + (end - start));
        log.info("result: " + result);
        log.info("Method: " + pjp.getSignature().getName());
        log.info("Class: " + pjp.getTarget().getClass().getName());

        log.info("========== logAround end ==========");
        return result;
    }

}
