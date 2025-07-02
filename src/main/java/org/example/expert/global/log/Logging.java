package org.example.expert.global.log;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.expert.domain.log.entity.IdSource;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.entity.LogType;
import org.example.expert.domain.log.repository.LogRepository;
import org.example.expert.global.common.ApiResponse;
import org.example.expert.global.config.JwtUtil;
import org.example.expert.global.log.annotation.LogWrite;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class Logging {
    private final HttpServletRequest request;
    private final LogRepository logRepository;
    private final JwtUtil jwtUtil;

    @Around("@annotation(logWrite)")
    @Transactional
    public Object save(ProceedingJoinPoint joinPoint, LogWrite logWrite) throws Throwable {
        Object result = null;
        LogType logType = null;
        String status = "SUCCESS";
        String errorMessage = null;
        Throwable throwable = null;

        try{
            result = joinPoint.proceed();
            return result;
        }catch (Throwable t){
            status="FAIL";
            errorMessage = t.getMessage();
            throw t;
        } finally {
            try {
                logType = logWrite.type();

                int userId = extractId(logType.getUserSource(), logType.getTargetKey(), result, joinPoint);
                int targetId = extractId(logType.getTargetSource(), logType.getTargetKey(), result, joinPoint);

                String requestMethod = request.getMethod();
                String requestURL = request.getRequestURI();
                String address = request.getRemoteAddr();

                Log log = new Log((long) targetId, (long) userId, logType, requestMethod, requestURL, status, address, LocalDateTime.now());
                log.setErrorMessage(errorMessage);

                logRepository.save(log);
            } catch (Exception e) {
                log.warn("Logging Error - 매니저 추가 시 에러 발생: {}", e.getMessage());
            }
        }
    }

    private int extractId(IdSource source, String targetKey, Object result, ProceedingJoinPoint joinPoint) {
        return switch (source) {
            case TOKEN -> extractFromToken(result);
            case PATH_VARIABLE -> extractFromPathVariable(joinPoint, targetKey);
            case RESPONSE -> extractFromResponse(result);
            case NULL -> -1;
        };
    }

    private int extractFromPathVariable(ProceedingJoinPoint joinPoint,String targetKey) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (param.isAnnotationPresent(PathVariable.class)) {
                PathVariable pv = param.getAnnotation(PathVariable.class);

                String name = pv.value().isEmpty() ? param.getName() : pv.value();

                if (name.equals(targetKey)) {
                    Object value = args[i];
                    if (value instanceof Integer) {
                        return (Integer) value;
                    }
                    if (value instanceof Long) {
                        return ((Long) value).intValue();
                    }
                }
            }
        }

        return -1;
    }

    private int extractFromToken(Object result) {
        if (request.getAttribute("id") == null) {
            if (result instanceof ApiResponse<?> responseEntity) {
                Map<String, String> body = (Map<String, String>) responseEntity.getData();
                String token = body.get("token");
                return jwtUtil.extractId(token);
            }
            return -1;
        } else {
            return (int) request.getAttribute("id");
        }
    }

    private int extractFromResponse(Object result) {
        if (result instanceof ApiResponse<?> responseEntity) {
            Object body = responseEntity.getData();
            if (body instanceof LoggableResponse loggable) {
                return loggable.getTargetId();
            }
        }
        return -1;
    }

}