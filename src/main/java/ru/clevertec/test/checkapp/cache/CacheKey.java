package ru.clevertec.test.checkapp.cache;

import lombok.Data;

import java.util.Objects;

@Data
public class CacheKey {
    private final String stringKey;
    private long lastAccessedTime;
    private int hitCount;
    public CacheKey(String stringKey) {
        this.stringKey = stringKey;
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
        CacheKey that = (CacheKey) o;
        return stringKey.equals(that.stringKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringKey);
    }
}
