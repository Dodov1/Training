package com.web.training.config.logger;

import com.web.training.models.entities.Log;
import com.web.training.repositories.LogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class JpaLogger {

    private final LogRepository logRepository;

    public JpaLogger(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Pointcut("execution(* org.springframework.security.authentication.AuthenticationManager.authenticate(..))")
    public void track() {
    }

    @AfterReturning(returning = "result", pointcut = "track()")
    public void after(JoinPoint joinPoint, Object result) throws Throwable {
        Log log = new Log();
        String username = ((Authentication) result).getName();
        String message = String.format("User With username: %s logged in successfully!", username);
        log.setMessage(message);
        log.setLocalDateTime(LocalDateTime.now());
        this.logRepository.saveAndFlush(log);
    }
}
