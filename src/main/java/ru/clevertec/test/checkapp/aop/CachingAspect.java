package ru.clevertec.test.checkapp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.clevertec.test.checkapp.model.CacheObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.expression.spel.support.StandardEvaluationContext;

@Aspect
@Component
public class CachingAspect {
    private final Map<String, CacheObject> lfuCache = new LinkedHashMap<>();
    private final Map<String, CacheObject> lruCache = new LinkedHashMap<>();

    @Around("@annotation(cacheable)")
    public Object cacheable(ProceedingJoinPoint joinPoint, GetCache cacheable) throws Throwable {
        String cacheName = cacheable.cacheNames();
        String key = cacheable.key();
        Class<?> returnType = cacheable.returnType();

        Map<String, CacheObject> cache = getCache(cacheName);

        // Generate cache key using SpEL expression
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(getVariableMap(joinPoint));
        ExpressionParser parser = new SpelExpressionParser();
        String evaluatedKey = parser.parseExpression(key).getValue(context, String.class);

        CacheObject cacheObject = cache.get(evaluatedKey);
        if (cacheObject != null) {
            return cacheObject.getValue();
        }

        Object result = joinPoint.proceed();

        cacheObject = new CacheObject(result, System.currentTimeMillis());
        cache.put(evaluatedKey, cacheObject);

        return result;
    }

//    @Around("@annotation(cachePut)")
//    public Object cachePut(ProceedingJoinPoint joinPoint, UpdateCache cachePut) throws Throwable {
//        Map<String, CacheObject> cache = getCache(cachePut.cacheName());
//        String cacheKey = getCacheKey(joinPoint, cachePut.key());
//        Object result = joinPoint.proceed();
//        if (result != null) {
//            cache.put(cacheKey, new CacheObject(result, cachePut.maxAgeSeconds()));
//        }
//        return result;
//    }
//
//    @Around("@annotation(cacheEvict)")
//    public Object cacheEvict(ProceedingJoinPoint joinPoint, DeleteCache cacheEvict) throws Throwable {
//        Map<String, CacheObject> cache = getCache(cacheEvict.cacheName());
//        String cacheKey = getCacheKey(joinPoint, cacheEvict.key());
//        cache.remove(cacheKey);
//        return joinPoint.proceed();
//    }

    private Map<String, CacheObject> getCache(String cacheName) {
        if ("lfuCache".equals(cacheName)) {
            return lfuCache;
        } else if ("lruCache".equals(cacheName)) {
            return lruCache;
        } else {
            throw new IllegalArgumentException("Unknown cache name: " + cacheName);
        }
    }

    private String getCacheKey(ProceedingJoinPoint joinPoint, String key) {
        if (StringUtils.hasText(key)) {
            return key;
        } else {
            return Arrays.toString(joinPoint.getArgs());
        }
    }

    private Map<String, Object> getVariableMap(ProceedingJoinPoint joinPoint) {
        Map<String, Object> variableMap = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {
            variableMap.put(parameterNames[i], args[i]);
        }

        return variableMap;
    }
}