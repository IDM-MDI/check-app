package ru.clevertec.test.checkapp.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.test.checkapp.cache.CacheHandler;
import ru.clevertec.test.checkapp.cache.CacheKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;

import ru.clevertec.test.checkapp.cache.DeleteCache;
import ru.clevertec.test.checkapp.cache.GetCache;
import ru.clevertec.test.checkapp.cache.SaveCache;
import ru.clevertec.test.checkapp.cache.UpdateCache;

@Aspect
@Component
@RequiredArgsConstructor
public class CachingAspect {
    private final Map<Class<?>,SortedSet<CacheKey>> classCache = new HashMap<>();
    private final CacheHandler handler;

    @Around("@annotation(getCache)")
    public Object cacheable(ProceedingJoinPoint joinPoint, GetCache getCache) throws Throwable {
        SortedSet<CacheKey> cache = handler.getClassCache(classCache,getCache.returnType());
        String evaluatedKey = handler.getKeyValueFromMethod(joinPoint, getCache.key());
        Optional<CacheKey> cacheObject = handler.findByKey(cache, evaluatedKey);
        if (cacheObject.isPresent()) {
            return handler.getPresentValue(cacheObject.get());
        }
        return handler.createNewCache(cache, evaluatedKey, joinPoint.proceed());
    }

    @AfterReturning(value = "@annotation(saveCache)",returning = "result")
    public void saveCache(SaveCache saveCache,Object result) throws NoSuchFieldException, IllegalAccessException {
        SortedSet<CacheKey> cache = handler.getClassCache(classCache,saveCache.returnType());
        String id = handler.getFieldValue(result,saveCache.fieldName());
        handler.createNewCache(cache, id, result);
    }

    @Around("@annotation(deleteCache)")
    public Object cacheable(ProceedingJoinPoint joinPoint, DeleteCache deleteCache) throws Throwable {
        String evaluatedKey = handler.getKeyValueFromMethod(joinPoint, deleteCache.key());
        SortedSet<CacheKey> cache = handler.getClassCache(classCache, deleteCache.returnType());
        return deleteFromCache(cache, joinPoint, evaluatedKey);
    }

    @Around("@annotation(updateCache)")
    public Object cacheable(ProceedingJoinPoint joinPoint, UpdateCache updateCache) throws Throwable {
        String evaluatedKey = handler.getKeyValueFromMethod(joinPoint, updateCache.key());
        SortedSet<CacheKey> cache = handler.getClassCache(classCache, updateCache.returnType());
        Object result = deleteFromCache(cache, joinPoint, evaluatedKey);
        return handler.createNewCache(cache, evaluatedKey, result);
    }

    private Object deleteFromCache(SortedSet<CacheKey> cache, ProceedingJoinPoint joinPoint, String evaluatedKey) throws Throwable {
        Optional<CacheKey> cacheObject = handler.findByKey(cache, evaluatedKey);
        Object result = joinPoint.proceed();
        cacheObject.ifPresent(k -> handler.deleteFromCache(cache,k));
        return result;
    }
}