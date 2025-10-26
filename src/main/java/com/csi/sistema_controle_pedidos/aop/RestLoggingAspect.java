package com.csi.sistema_controle_pedidos.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class RestLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(RestLoggingAspect.class);

    // Controllers
    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logControllers(ProceedingJoinPoint pjp) throws Throwable {
        String sig = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();
        long start = System.currentTimeMillis();
        try {
            log.debug("HTTP -> {} args={}", sig, safeArgs(args));
            Object result = pjp.proceed();
            log.debug("HTTP <- {} took={}ms", sig, System.currentTimeMillis() - start);
            return result;
        } catch (Throwable t) {
            log.error("HTTP !! {} took={}ms ex={}: {}", sig, System.currentTimeMillis() - start, t.getClass().getSimpleName(), t.getMessage());
            throw t;
        }
    }

    // Services
    @Around("within(@org.springframework.stereotype.Service *)")
    public Object logServices(ProceedingJoinPoint pjp) throws Throwable {
        String sig = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            log.trace("SERVICE <- {} took={}ms", sig, System.currentTimeMillis() - start);
            return result;
        } catch (Throwable t) {
            log.warn("SERVICE !! {} took={}ms ex={}: {}", sig, System.currentTimeMillis() - start, t.getClass().getSimpleName(), t.getMessage());
            throw t;
        }
    }

    private String safeArgs(Object[] args) {
        try { return Arrays.toString(args); } catch (Exception e) { return "<unprintable>"; }
    }
}
