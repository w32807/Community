package com.jwj.community.web.advice.controllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Aspect
@Slf4j
@ControllerAdvice
public class ControllerLoggerAdvice {

    @Pointcut("execution(* com.jwj.community.web.*.controller.*.*(..))")
    public void controllerLogger() {}

    @Before("controllerLogger()")
    public void beforeLogPrint(JoinPoint joinPoint) {
        log.info(joinPoint.getSignature().getDeclaringTypeName()  + " - " + joinPoint.getSignature().getName() + ".run()");
    }
}
