package ru.clevertec.test.checkapp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import ru.clevertec.test.checkapp.cache.CacheObject;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

import org.springframework.expression.spel.support.StandardEvaluationContext;
import ru.clevertec.test.checkapp.cache.GetCache;
import ru.clevertec.test.checkapp.cache.SaveCache;

@Aspect
@Component
public class CachingAspect {
    @Value("${cache.type}")
    private String cacheType;
    private final TreeSet<CacheObject> lfuCache = new TreeSet<>(
            Comparator.comparing(CacheObject::getHitCount)
                    .reversed()
    );
    private final TreeSet<CacheObject> lruCache = new TreeSet<>(
            Comparator.comparing(CacheObject::getLastAccessedTime)
                    .reversed()
    );

    @Around("@annotation(getCache)")
    public Object cacheable(ProceedingJoinPoint joinPoint, GetCache getCache) throws Throwable {
        TreeSet<CacheObject> cache = getCache(cacheType);
        String evaluatedKey = getKeyValueFromMethod(joinPoint, getCache.key());
        Optional<CacheObject> cacheObject = findByKey(cache, evaluatedKey);
        if (cacheObject.isPresent()) {
            return getPresentValue(cacheObject.get());
        }
        return createNewCache(cache, evaluatedKey, joinPoint.proceed());
    }

    @AfterReturning(value = "@annotation(saveCache)",returning = "result")
    public void saveCache(SaveCache saveCache,Object result) throws NoSuchFieldException, IllegalAccessException {
        String id = getFieldValue(result,saveCache.fieldName());
        TreeSet<CacheObject> cache = getCache(cacheType);
        createNewCache(cache, id, result);
    }

    private static String getFieldValue(Object result,String field) throws NoSuchFieldException, IllegalAccessException {
        Field idField = result.getClass()
                .getDeclaredField(field);
        idField.setAccessible(true);
        return idField.get(result).toString();
    }

    private static Object getPresentValue(CacheObject cacheObject) {
        cacheObject.hit();
        return cacheObject.getValue();
    }

    private static Object createNewCache(TreeSet<CacheObject> cache, String evaluatedKey, Object result) {
        cache.add(new CacheObject(evaluatedKey, result));
        return result;
    }

    private String getKeyValueFromMethod(JoinPoint joinPoint, String key) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(getVariableMap(joinPoint));
        return new SpelExpressionParser().parseExpression(key).getValue(context, String.class);
    }

    private static Optional<CacheObject> findByKey(TreeSet<CacheObject> cache, String evaluatedKey) {
        return cache.stream()
                .filter(cacheObjectEntry -> cacheObjectEntry.getKey().equals(evaluatedKey))
                .findAny();
    }
    private TreeSet<CacheObject> getCache(String cacheName) {
        switch (cacheName) {
            case "lfu" -> {
                return lfuCache;
            }
            case "lru" -> {
                return lruCache;
            }
            default -> throw new IllegalArgumentException("Unknown cache name: " + cacheName);
        }
    }

    private Map<String, Object> getVariableMap(JoinPoint joinPoint) {
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