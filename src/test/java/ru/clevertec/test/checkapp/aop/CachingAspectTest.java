package ru.clevertec.test.checkapp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.test.checkapp.cache.CacheHandler;
import ru.clevertec.test.checkapp.cache.CacheKey;
import ru.clevertec.test.checkapp.cache.DeleteCache;
import ru.clevertec.test.checkapp.cache.GetCache;
import ru.clevertec.test.checkapp.cache.SaveCache;
import ru.clevertec.test.checkapp.cache.UpdateCache;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CachingAspectTest {
    @Mock
    private CacheHandler cacheHandler;
    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private GetCache getCache;
    @Mock
    private SaveCache saveCache;

    @Mock
    private DeleteCache deleteCache;
    @Mock
    private UpdateCache updateCache;
    @InjectMocks
    private CachingAspect cachingAspect;
    private SortedSet<CacheKey> cache;

    @BeforeEach
    void setup() {
        cache = new TreeSet<>(Comparator.comparing(CacheKey::getStringKey));
    }

    @Test
    void getCacheMethodWithExistingCache() throws Throwable {
        Object expected = "value";
        CacheKey cacheKey = new CacheKey("key", "value");
        cache.add(cacheKey);
        when(cacheHandler.getClassCache(any(), any())).thenReturn(cache);
        when(cacheHandler.getKeyValueFromMethod(any(), any())).thenReturn("key");
        when(cacheHandler.findByKey(any(), any())).thenReturn(Optional.of(cacheKey));

        Object result = cachingAspect.getCacheAdvise(joinPoint, getCache);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void cacheMethodWithNewCache() throws Throwable {
        Object expected = "value";
        when(cacheHandler.getClassCache(any(), any())).thenReturn(cache);
        when(cacheHandler.getKeyValueFromMethod(any(), any())).thenReturn("key");
        when(cacheHandler.findByKey(any(), any())).thenReturn(Optional.empty());
        when(joinPoint.proceed()).thenReturn(expected);
        when(cacheHandler.createNewCache(cache,"key",expected))
                .thenReturn(expected);
        Object result = cachingAspect.getCacheAdvise(joinPoint, getCache);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void saveCacheMethod() throws NoSuchFieldException, IllegalAccessException {
        Object result = new Object();
        when(cacheHandler.getClassCache(any(), any())).thenReturn(cache);
        when(cacheHandler.getFieldValue(any(), any())).thenReturn("id");
        when(cacheHandler.createNewCache(cache,"id",result))
                .thenCallRealMethod();
        cachingAspect.saveCacheAdvise(saveCache, result);

        Assertions.assertThat(cache).hasSize(1);
    }

    @Test
    void deleteCache() throws Throwable {
        CacheKey cacheKey = new CacheKey("key", "value");
        cache.add(cacheKey);
        when(cacheHandler.getClassCache(any(), any())).thenReturn(cache);
        when(cacheHandler.getKeyValueFromMethod(any(), any())).thenReturn("key");
        when(cacheHandler.findByKey(any(), any())).thenReturn(Optional.of(cacheKey));
        when(joinPoint.proceed()).thenReturn("result");

        Object result = cachingAspect.deleteCacheAdvise(joinPoint, deleteCache);

        Assertions.assertThat(result).isEqualTo("result");
    }
}