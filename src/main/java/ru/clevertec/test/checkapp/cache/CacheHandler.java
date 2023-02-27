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

@Component
public class CacheHandler {
    @Value("${cache.type}")
    private String cacheType;
    private Comparator<CacheKey> lfuComparator = Comparator
            .comparing(CacheKey::getHitCount)
            .reversed();
    private Comparator<CacheKey> lruComparator = Comparator
            .comparing(CacheKey::getLastAccessedTime)
            .reversed();

    public String getFieldValue(Object result,String field) throws NoSuchFieldException, IllegalAccessException {
        Field idField = result.getClass()
                .getDeclaredField(field);
        idField.setAccessible(true);
        return idField.get(result).toString();
    }

    public Object getPresentValue(CacheKey cacheKey) {
        cacheKey.hit();
        return cacheKey.getValue();
    }

    public String getKeyValueFromMethod(JoinPoint joinPoint, String key) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(getVariableMap(joinPoint));
        return new SpelExpressionParser().parseExpression(key).getValue(context, String.class);
    }

    public Optional<CacheKey> findByKey(SortedSet<CacheKey> cache, String evaluatedKey) {
        return cache.stream()
                .sorted(cacheType.equals("lru") ? lruComparator : lfuComparator)
                .filter(cacheKeyEntry -> cacheKeyEntry.getStringKey().equals(evaluatedKey))
                .findAny();
    }

    public Map<String, Object> getVariableMap(JoinPoint joinPoint) {
        Map<String, Object> variableMap = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        IntStream.range(0, args.length)
                .forEach(i -> variableMap.put(parameterNames[i], args[i]));

        return variableMap;
    }
    public Object createNewCache(SortedSet<CacheKey> cache, String evaluatedKey, Object result) {
        CacheKey cacheKey = new CacheKey(evaluatedKey, result);
        cache.add(cacheKey);
        return result;
    }

    public SortedSet<CacheKey> getClassCache(Map<Class<?>,SortedSet<CacheKey>> classCache, Class<?> returnType) {
        return classCache.containsKey(returnType) ? classCache.get(returnType) : createSortedSet(classCache,returnType);
    }

    private SortedSet<CacheKey> createSortedSet(Map<Class<?>,SortedSet<CacheKey>> cache,Class<?> className) {
        cache.put(className,new TreeSet<>(Comparator.comparing(CacheKey::getStringKey)));
        return cache.get(className);
    }
}
