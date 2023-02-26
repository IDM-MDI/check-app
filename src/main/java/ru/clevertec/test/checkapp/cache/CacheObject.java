package ru.clevertec.test.checkapp.cache;

import lombok.Data;

@Data
public class CacheObject {
    private Object value;
    private String key;
    private long lastAccessedTime;
    private int hitCount;
    public CacheObject(String key, Object value) {
        this.key = key;
        this.value = value;
        this.lastAccessedTime = System.currentTimeMillis();
        this.hitCount = 1;
    }
    public void hit() {
        hitCount++;
        lastAccessedTime = System.currentTimeMillis();
    }

}
