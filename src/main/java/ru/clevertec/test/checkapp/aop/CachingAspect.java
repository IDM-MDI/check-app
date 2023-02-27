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


/**
 * CachingAspect is an aspect that provides caching functionality for annotated methods.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CachingAspect {
    /**
     * A map that stores cache data for each class.
     */
    private final Map<Class<?>,SortedSet<CacheKey>> classCache = new HashMap<>();

    /**
     * An object that handles cache operations.
     */
    private final CacheHandler handler;


    /**
     * A method-level advice that intercepts methods annotated with {@link GetCache} annotation
     * and provides caching functionality.
     * @param joinPoint The join point that represents the method execution.
     * @param getCache The annotation that indicates the method is cacheable.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("@annotation(getCache)")
    public Object getCacheAdvise(ProceedingJoinPoint joinPoint, GetCache getCache) throws Throwable {
        SortedSet<CacheKey> cache = handler.getClassCache(classCache,getCache.returnType());
        String evaluatedKey = handler.getKeyValueFromMethod(joinPoint, getCache.key());
        Optional<CacheKey> cacheObject = handler.findByKey(cache, evaluatedKey);
        if (cacheObject.isPresent()) {
            return handler.getPresentValue(cacheObject.get());
        }
        return handler.createNewCache(cache, evaluatedKey, joinPoint.proceed());
    }


    /**
     * A method-level advice that intercepts methods annotated with {@link SaveCache} annotation
     * and saves the result to the cache.
     * @param saveCache The annotation that indicates the method saves data to the cache.
     * @param result The result of the method execution.
     * @throws NoSuchFieldException If the specified field does not exist.
     * @throws IllegalAccessException If the specified field is not accessible.
     */
    @AfterReturning(value = "@annotation(saveCache)",returning = "result")
    public void saveCacheAdvise(SaveCache saveCache,Object result) throws NoSuchFieldException, IllegalAccessException {
        SortedSet<CacheKey> cache = handler.getClassCache(classCache,saveCache.returnType());
        String id = handler.getFieldValue(result,saveCache.fieldName());
        handler.createNewCache(cache, id, result);
    }


    /**
     * A method-level advice that intercepts methods annotated with {@link DeleteCache} annotation
     * and removes the cached value associated with the specified key.
     * @param joinPoint The join point that represents the method execution.
     * @param deleteCache The annotation that indicates the method deletes data from the cache.
     * @return The result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("@annotation(deleteCache)")
    public Object deleteCacheAdvise(ProceedingJoinPoint joinPoint, DeleteCache deleteCache) throws Throwable {
        String evaluatedKey = handler.getKeyValueFromMethod(joinPoint, deleteCache.key());
        SortedSet<CacheKey> cache = handler.getClassCache(classCache, deleteCache.returnType());
        Optional<CacheKey> cacheObject = handler.findByKey(cache, evaluatedKey);
        Object result = joinPoint.proceed();
        cacheObject.ifPresent(cache::remove);
        return result;
    }


    /**
     * A method-level advice that intercepts methods annotated with {@link UpdateCache} annotation
     * and updates the cached value associated with the specified key.
     * @param joinPoint The join point that represents the method execution.
     * @param updateCache The annotation that indicates the method updates data in the cache.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("@annotation(updateCache)")
    public Object updateCacheAdvise(ProceedingJoinPoint joinPoint, UpdateCache updateCache) throws Throwable {
        String evaluatedKey = handler.getKeyValueFromMethod(joinPoint, updateCache.key());
        SortedSet<CacheKey> cache = handler.getClassCache(classCache, updateCache.returnType());
        Optional<CacheKey> cacheObject = handler.findByKey(cache, evaluatedKey);
        Object result = joinPoint.proceed();
        cacheObject.ifPresent(k -> k.setValue(result));
        return handler.createNewCache(cache, evaluatedKey, result);
    }
}