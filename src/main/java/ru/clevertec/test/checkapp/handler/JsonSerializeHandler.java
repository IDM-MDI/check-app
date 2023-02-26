package ru.clevertec.test.checkapp.handler;


import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static ru.clevertec.test.checkapp.model.JsonStringField.STRING_QUOT;
import static ru.clevertec.test.checkapp.validator.JSONClassValidator.isText;

@Component
public class JsonSerializeHandler {
    public <T> String joinToString(List<?> objects, Function<T,String> converter) {
        StringBuilder builder = new StringBuilder();
        objects.forEach(o -> builder.append(converter.apply((T) o)).append(","));
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
    @SneakyThrows
    public String convertToPrimitive(Object obj) {
        return isText(obj) ? convertToText(obj) : obj.toString();
    }

    @SneakyThrows
    public String convertToText(Object obj) {
        return STRING_QUOT + obj.toString() + STRING_QUOT;
    }
}
