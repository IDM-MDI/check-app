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
import ru.clevertec.test.checkapp.cache.CacheKey;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.expression.spel.support.StandardEvaluationContext;
import ru.clevertec.test.checkapp.cache.DeleteCache;
import ru.clevertec.test.checkapp.cache.GetCache;
import ru.clevertec.test.checkapp.cache.SaveCache;
import ru.clevertec.test.checkapp.cache.UpdateCache;

@Aspect
@Component
public class CachingAspect {
    @Value("${cache.type}")
    private String cacheType;
    private final TreeMap<CacheKey, Object> lfuCache = new TreeMap<>(
            Comparator.comparing(CacheKey::getHitCount)
                    .reversed()
    );
    private final TreeMap<CacheKey, Object> lruCache = new TreeMap<>(
            Comparator.comparing(CacheKey::getLastAccessedTime)
                    .reversed()
    );

    @Around("@annotation(getCache)")
    public Object cacheable(ProceedingJoinPoint joinPoint, GetCache getCache) throws Throwable {
        TreeMap<CacheKey, Object> cache = getCache(cacheType);
        String evaluatedKey = getKeyValueFromMethod(joinPoint, getCache.key());
        Optional<CacheKey> cacheObject = findByKey(cache, evaluatedKey);
        if (cacheObject.isPresent()) {
            return getPresentValue(cacheObject.get(),cache);
        }
        return createNewCache(cache, evaluatedKey, joinPoint.proceed());
    }

    private static Object createNewCache(TreeMap<CacheKey, Object> cache, String evaluatedKey, Object result) {
        cache.put(new CacheKey(evaluatedKey),result);
        return result;
    }
    @AfterReturning(value = "@annotation(saveCache)",returning = "result")
    public void saveCache(SaveCache saveCache,Object result) throws NoSuchFieldException, IllegalAccessException {
        String id = getFieldValue(result,saveCache.fieldName());
        TreeMap<CacheKey, Object> cache = getCache(cacheType);
        createNewCache(cache, id, result);
    }

    @Around("@annotation(deleteCache)")
    public Object cacheable(ProceedingJoinPoint joinPoint, DeleteCache deleteCache) throws Throwable {
        TreeMap<CacheKey, Object> cache = getCache(cacheType);
        String evaluatedKey = getKeyValueFromMethod(joinPoint, deleteCache.key());
        Optional<CacheKey> cacheObject = findByKey(cache, evaluatedKey);
        Object result = joinPoint.proceed();
        cacheObject.ifPresent(k -> deleteFromCache(cache,k));
        return result;
    }

    @Around("@annotation(updateCache)")
    public Object cacheable(ProceedingJoinPoint joinPoint, UpdateCache updateCache) throws Throwable {
        TreeMap<CacheKey, Object> cache = getCache(cacheType);
        String evaluatedKey = getKeyValueFromMethod(joinPoint, updateCache.key());
        Optional<CacheKey> cacheObject = findByKey(cache, evaluatedKey);
        Object result = joinPoint.proceed();
        cacheObject.ifPresent(object -> deleteFromCache(cache, object));
        return createNewCache(cache, evaluatedKey, result);
    }

    private void deleteFromCache(TreeMap<CacheKey, Object> cache, CacheKey cacheKey) {
        cache.remove(cacheKey);
    }


    private static String getFieldValue(Object result,String field) throws NoSuchFieldException, IllegalAccessException {
        Field idField = result.getClass()
                .getDeclaredField(field);
        idField.setAccessible(true);
        return idField.get(result).toString();
    }



    private static Object getPresentValue(CacheKey cacheKey, TreeMap<CacheKey, Object> cache) {
        Object result = cache.get(cacheKey);
        cache.remove(cacheKey);
        cacheKey.hit();
        cache.put(cacheKey,result);
        return result;
    }

    private String getKeyValueFromMethod(JoinPoint joinPoint, String key) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(getVariableMap(joinPoint));
        return new SpelExpressionParser().parseExpression(key).getValue(context, String.class);
    }

    private static Optional<CacheKey> findByKey(TreeMap<CacheKey, Object> cache, String evaluatedKey) {
        return cache.keySet()
                .stream()
                .filter(cacheKeyEntry -> cacheKeyEntry.getStringKey().equals(evaluatedKey))
                .findAny();
    }
    private TreeMap<CacheKey, Object> getCache(String cacheName) {
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