package ru.clevertec.test.checkapp.cache;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@Data
public class CacheKey {
    private final String stringKey;
    private Object value;
    private long lastAccessedTime;
    private int hitCount;

    public CacheKey(String stringKey, Object value) {
        this.stringKey = stringKey;
        this.value = value;
        this.lastAccessedTime = System.currentTimeMillis();
        this.hitCount = 1;
    }
    public void hit() {
        hitCount++;
        lastAccessedTime = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        return lastAccessedTime == cacheKey.lastAccessedTime && hitCount == cacheKey.hitCount && stringKey.equals(cacheKey.stringKey) && Objects.equals(value, cacheKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringKey, value, lastAccessedTime, hitCount);
    }
}
