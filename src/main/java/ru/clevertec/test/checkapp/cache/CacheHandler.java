package ru.clevertec.test.checkapp.cache;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.IntStream;

/**
 * The CacheHandler class provides methods to handle caching of method results.
 * It supports two types of cache: Least Recently Used (LRU) and Least Frequently Used (LFU).
 */
@Component
public class CacheHandler {
    /**
     * The cacheType field stores the type of cache being used, which is specified in the application properties file.
     */
    @Value("${cache.type}")
    private String cacheType;

    /**
     * The lfuComparator field stores a Comparator that sorts CacheKeys in order of least frequently used.
     */
    private Comparator<CacheKey> lfuComparator = Comparator
            .comparing(CacheKey::getHitCount)
            .reversed();

    /**
     * The lruComparator field stores a Comparator that sorts CacheKeys in order of least recently used.
     */
    private Comparator<CacheKey> lruComparator = Comparator
            .comparing(CacheKey::getLastAccessedTime)
            .reversed();


    /**
     * The getFieldValue method retrieves the value of a specific field within an object.
     * @param result The object to retrieve the field value from.
     * @param field The name of the field to retrieve the value of.
     * @return The value of the specified field within the provided object.
     * @throws NoSuchFieldException If the specified field does not exist within the object.
     * @throws IllegalAccessException If the specified field is not accessible.
     */
    public String getFieldValue(Object result,String field) throws NoSuchFieldException, IllegalAccessException {
        Field idField = result.getClass()
                .getDeclaredField(field);
        idField.setAccessible(true);
        return idField.get(result).toString();
    }


    /**
     * The getPresentValue method retrieves the value of a cache entry and increments its hit count.
     * @param cacheKey The CacheKey for the value to retrieve.
     * @return The value associated with the provided CacheKey.
     */
    public Object getPresentValue(CacheKey cacheKey) {
        cacheKey.hit();
        return cacheKey.getValue();
    }


    /**
     * The getKeyValueFromMethod method retrieves the key for a cache entry by evaluating a SpEL expression.
     * @param joinPoint The JoinPoint object for the method call.
     * @param key The SpEL expression used to retrieve the cache key.
     * @return The cache key as a String.
     */
    public String getKeyValueFromMethod(JoinPoint joinPoint, String key) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(getVariableMap(joinPoint));
        return new SpelExpressionParser().parseExpression(key).getValue(context, String.class);
    }


    /**
     * The findByKey method searches a SortedSet of CacheKeys for a specific key.
     * @param cache The SortedSet of CacheKeys to search.
     * @param evaluatedKey The key to search for.
     * @return An Optional containing the CacheKey associated with the provided key, or an empty Optional if no such CacheKey exists.
     */
    public Optional<CacheKey> findByKey(SortedSet<CacheKey> cache, String evaluatedKey) {
        return cache.stream()
                .sorted(cacheType.equals("lru") ? lruComparator : lfuComparator)
                .filter(cacheKeyEntry -> cacheKeyEntry.getStringKey().equals(evaluatedKey))
                .findAny();
    }


    /**
     * The getVariableMap method retrieves a Map of parameter names and values from a JoinPoint object.
     * @param joinPoint The JoinPoint object for the method call.
     * @return A Map containing parameter names and values.
     */
    public Map<String, Object> getVariableMap(JoinPoint joinPoint) {
        Map<String, Object> variableMap = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        IntStream.range(0, args.length)
                .forEach(i -> variableMap.put(parameterNames[i], args[i]));

        return variableMap;
    }

    /**
     * The createNewCache method creates a new CacheKey and adds it to the provided SortedSet.
     * @param cache The SortedSet to add the new CacheKey to.
     * @param evaluatedKey The key associated with the new CacheKey.
     * @param result The value to store in the new CacheKey.
     * @return The value associated with the new CacheKey.
     */
    public Object createNewCache(SortedSet<CacheKey> cache, String evaluatedKey, Object result) {
        CacheKey cacheKey = new CacheKey(evaluatedKey, result);
        cache.add(cacheKey);
        return result;
    }


    /**
     * This method returns the class cache from the provided map of class caches.
     * If a class cache already exists for the given return type, it returns that cache.
     * If not, it creates a new sorted set cache for the class and adds it to the map before returning it.
     * @param classCache A map of class caches
     * @param returnType The return type of the method whose cache is being accessed
     * @return A sorted set cache for the given return type
     */
    public SortedSet<CacheKey> getClassCache(Map<Class<?>,SortedSet<CacheKey>> classCache, Class<?> returnType) {
        return classCache.containsKey(returnType) ? classCache.get(returnType) : createSortedSet(classCache,returnType);
    }


    /**
     * This method creates a new sorted set cache for the given class name and adds it to the provided map of class caches.
     * It uses the string key comparator to sort the cache elements by their string keys.
     * @param cache A map of class caches
     * @param className The class name for which to create the cache
     * @return A new sorted set cache for the given class name
     */
    private SortedSet<CacheKey> createSortedSet(Map<Class<?>,SortedSet<CacheKey>> cache,Class<?> className) {
        cache.put(className,new TreeSet<>(Comparator.comparing(CacheKey::getStringKey)));
        return cache.get(className);
    }
}