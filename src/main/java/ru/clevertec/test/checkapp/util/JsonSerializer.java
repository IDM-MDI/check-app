package ru.clevertec.test.checkapp.util;

import java.util.List;

public interface JsonSerializer {
    String serialize(Object object);
    String serialize(Object... objects);
    String serialize(List<?> objects);
}
