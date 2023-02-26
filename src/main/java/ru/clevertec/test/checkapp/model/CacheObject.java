package ru.clevertec.test.checkapp.model;

public class CacheObject<T> {
    private final T value;
    private final long maxAgeSeconds;
    private long lastAccessedTime;
    private int hitCount;
    public CacheObject(T value, long maxAgeSeconds) {
        this.value = value;
        this.maxAgeSeconds = maxAgeSeconds;
        this.lastAccessedTime = System.currentTimeMillis();
        this.hitCount = 0;
    }

    public Object getValue() {
        return value;
    }

    public long getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void hit() {
        hitCount++;
        lastAccessedTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - lastAccessedTime > maxAgeSeconds * 1000;
    }
}
