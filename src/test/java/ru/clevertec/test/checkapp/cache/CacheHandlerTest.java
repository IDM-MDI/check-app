package ru.clevertec.test.checkapp.cache;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class CacheHandlerTest {
    @Mock
    private JoinPoint joinPoint;
    @Mock
    private MethodSignature signature;
    private CacheHandler handler;
    private ProductModel productModel;
    private CacheKey cacheKey;
    private SortedSet<CacheKey> cache;

    @BeforeEach
    void setup() {
        handler = new CacheHandler();
        productModel = ProductModel.builder()
                .id(1L)
                .name("John Doe")
                .price(30)
                .offer(true)
                .build();
        cacheKey = new CacheKey("1", productModel);
        cache = new TreeSet<>(Comparator.comparing(CacheKey::getStringKey));
    }
    @Nested
    class GetFieldValue {
        @Test
        void getFieldValueShouldReturnCorrectName() throws NoSuchFieldException, IllegalAccessException {
            String fieldName = "name";
            String fieldValue = handler.getFieldValue(productModel, fieldName);
            Assertions.assertThat(fieldValue).isEqualTo("John Doe");
        }

        @Test
        void getFieldValueShouldReturnCorrectPrice() throws NoSuchFieldException, IllegalAccessException {
            String fieldName = "price";
            String fieldValue = handler.getFieldValue(productModel, fieldName);
            Assertions.assertThat(fieldValue).isEqualTo("30.0");
        }
        @Test
        void getFieldValueShouldThrowNoSuchFieldException() {
            String fieldName = "invalid_field";
            Assertions.assertThatThrownBy(() -> handler.getFieldValue(productModel, fieldName))
                    .isInstanceOf(NoSuchFieldException.class)
                    .hasMessageContaining(fieldName);
        }
        @Test
        void getFieldValueShouldReturnThrowNPE() {
            productModel = null;
            String fieldName = "name";
            Assertions.assertThatThrownBy(() -> handler.getFieldValue(productModel, fieldName))
                    .isInstanceOf(NullPointerException.class);
        }
    }
    @Nested
    class GetPresentValue {
        @Test
        void getPresentValueShouldReturnCorrectObject() throws NoSuchFieldException, IllegalAccessException {
            Object presentValue = handler.getPresentValue(cacheKey);
            Assertions.assertThat(presentValue).isEqualTo(productModel);
        }
        @Test
        void getFieldValueShouldReturnThrowNPE() {
            cacheKey = null;
            Assertions.assertThatThrownBy(() -> handler.getPresentValue(cacheKey))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    class GetKeyValueFromMethod {
        @Test
        void getKeyValueFromMethodShouldReturnCorrectPrimitive() {
            String id = "id";
            when(joinPoint.getArgs()).thenReturn(new Object[]{1});
            when(joinPoint.getSignature()).thenReturn(signature);
            when(signature.getParameterNames()).thenReturn(new String[]{id});

            Object result = handler.getKeyValueFromMethod(joinPoint,"#id");

            Assertions.assertThat(result).isEqualTo("1");
        }

        @Test
        void getKeyValueFromMethodShouldReturnCorrectObject() {
            String id = "#model.id";
            when(joinPoint.getArgs()).thenReturn(new Object[]{productModel});
            when(joinPoint.getSignature()).thenReturn(signature);
            when(signature.getParameterNames()).thenReturn(new String[]{"model"});

            Object result = handler.getKeyValueFromMethod(joinPoint,id);

            Assertions.assertThat(result).isEqualTo("1");
        }

        @Test
        void getKeyValueFromMethodShouldReturnThrowSpelEvaluationException() {
            String id = "id";
            when(joinPoint.getArgs()).thenReturn(new Object[]{1});
            when(joinPoint.getSignature()).thenReturn(signature);
            when(signature.getParameterNames()).thenReturn(new String[]{id});
            Assertions.assertThatThrownBy(() -> handler.getKeyValueFromMethod(joinPoint,id))
                    .isInstanceOf(SpelEvaluationException.class);
        }

        @Test
        void getKeyValueFromMethodShouldReturnThrowOutOfBoundsException() {
            String id = "id";
            when(joinPoint.getArgs()).thenReturn(new Object[]{1,2});
            when(joinPoint.getSignature()).thenReturn(signature);
            when(signature.getParameterNames()).thenReturn(new String[]{id});

            Assertions.assertThatThrownBy(() -> handler.getKeyValueFromMethod(joinPoint,id))
                    .isInstanceOf(ArrayIndexOutOfBoundsException.class);
        }
        @Test
        void getKeyValueFromMethodShouldReturnThrowNPE() {
            Assertions.assertThatThrownBy(() -> handler.getKeyValueFromMethod(joinPoint,null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    class FindByKey {
        @Test
        void findByKeyShouldExist() {
            CacheKey cacheKey1 = new CacheKey("key1", "value1");
            CacheKey cacheKey2 = new CacheKey("key2", "value2");
            cache.add(cacheKey1);
            cache.add(cacheKey2);
            Optional<CacheKey> result1 = handler.findByKey(cache, "key1");
            Assertions.assertThat(result1).isPresent().contains(cacheKey1);
        }
        @Test
        void findByKeyWhenCacheEmptyShouldNotExist() {
            Optional<CacheKey> result3 = handler.findByKey(cache, "key1");
            Assertions.assertThat(result3).isNotPresent();
        }

        @Test
        void findByKeyShouldNotExist() {
            CacheKey cacheKey1 = new CacheKey("key1", "value1");
            CacheKey cacheKey2 = new CacheKey("key2", "value2");
            cache.add(cacheKey1);
            cache.add(cacheKey2);
            Optional<CacheKey> result2 = handler.findByKey(cache, "key3");
            Assertions.assertThat(result2).isNotPresent();
        }
    }
    @Nested
    class CreateNewCache {
        @Test
        void createNewCacheShouldBeCorrectAddNewToEmptyCache() {
            Object result = new Object();
            Object returnedResult = handler.createNewCache(cache, "key1", result);
            Assertions.assertThat(returnedResult).isEqualTo(result);
        }
        @Test
        void createNewCacheShouldBeCorrectAddNewToNonEmpty() {
            CacheKey cacheKey1 = new CacheKey("key1", "value1");
            CacheKey cacheKey2 = new CacheKey("key2", "value2");
            cache.add(cacheKey1);
            cache.add(cacheKey2);
            Object result2 = new Object();
            Object returnedResult2 = handler.createNewCache(cache, "key3", result2);
            Assertions.assertThat(returnedResult2).isEqualTo(result2);
        }
        @Test
        void createNewCacheShouldBeCorrectWithExistingKey() {
            Object result3 = new Object();
            CacheKey cacheKey1 = new CacheKey("key1", "value1");
            CacheKey cacheKey2 = new CacheKey("key2", "value2");
            cache.add(cacheKey1);
            cache.add(cacheKey2);
            Object returnedResult3 = handler.createNewCache(cache, "key1", result3);
            Assertions.assertThat(returnedResult3).isEqualTo(result3);
        }
    }
    @Test
    void getClassCacheWhenExist() {
        Map<Class<?>, SortedSet<CacheKey>> classCache = new HashMap<>();
        classCache.put(String.class, cache);
        SortedSet<CacheKey> result1 = handler.getClassCache(classCache, String.class);
        Assertions.assertThat(result1).isSameAs(cache);
    }

    @Test
    void getClassCacheWhenDoesntExist() {
        Map<Class<?>, SortedSet<CacheKey>> classCache = new HashMap<>();
        SortedSet<CacheKey> result2 = handler.getClassCache(classCache, Integer.class);
        Assertions.assertThat(result2).isNotNull().isEmpty();
    }
}