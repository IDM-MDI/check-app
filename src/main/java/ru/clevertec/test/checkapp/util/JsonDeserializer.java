package ru.clevertec.test.checkapp.util;

public interface JsonDeserializer {
    <T> T deserialize(String json, Class<T> clazz);
}
