package ru.clevertec.test.checkapp.util.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import ru.clevertec.test.checkapp.handler.JsonSerializeHandler;
import ru.clevertec.test.checkapp.util.JsonDeserializer;
import ru.clevertec.test.checkapp.util.JsonSerializer;
import ru.clevertec.test.checkapp.validator.JSONClassValidator;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static ru.clevertec.test.checkapp.model.JsonStringField.JSON_ARRAY_EMPTY;
import static ru.clevertec.test.checkapp.model.JsonStringField.JSON_ARRAY_END;
import static ru.clevertec.test.checkapp.model.JsonStringField.JSON_ARRAY_START;
import static ru.clevertec.test.checkapp.model.JsonStringField.JSON_END;
import static ru.clevertec.test.checkapp.model.JsonStringField.JSON_FIELD_SEPARATOR;
import static ru.clevertec.test.checkapp.model.JsonStringField.JSON_START;
import static ru.clevertec.test.checkapp.model.JsonStringField.NULL_STRING;
import static ru.clevertec.test.checkapp.model.JsonStringField.STRING_QUOT;
@Component
@RequiredArgsConstructor
public class JsonConverter implements JsonSerializer, JsonDeserializer {
    private final JsonSerializeHandler handler;
    private final Gson gson;
    @SneakyThrows
    public String serialize(Object obj) {
        return objectSerializeValidChecker(obj).orElseGet(() -> serializeObject(obj));
    }

    public String serialize(Object... objects) {
        return serialize(Arrays.stream(objects).toList());
    }

    public String serialize(List<?> objects) {
        return listSerializeValidChecker(objects)
                .orElseGet(() -> JSON_ARRAY_START + handler.joinToString(objects, this::serialize) + JSON_ARRAY_END);
    }

    @SneakyThrows
    private String serializeObject(Object obj) {
        List<Field> fields = Arrays.stream(obj.getClass().getDeclaredFields()).toList();
        if(fields.isEmpty()) {
            throw new HttpMediaTypeNotAcceptableException("No acceptable representation");
        }
        return JSON_START + handler.joinToString(fields, o -> convertField((Field) o, obj)) + JSON_END;
    }

    private Optional<String> objectSerializeValidChecker(Object obj) {
        if(Objects.isNull(obj)) {
            return Optional.of(NULL_STRING);
        }
        else if(obj.getClass().isArray()) {
            return Optional.of(serialize((Object[]) obj));
        }
        else if(obj instanceof List<?>) {
            return Optional.of(serialize((List<?>) obj));
        }
        else if(JSONClassValidator.isPrimitive(obj)) {
            return Optional.of(handler.convertToPrimitive(obj));
        }
        return Optional.empty();
    }
    private Optional<String> listSerializeValidChecker(List<?> objects) {
        if(Objects.isNull(objects)) {
            return Optional.of(NULL_STRING);
        }
        else if(objects.isEmpty()) {
            return Optional.of(JSON_ARRAY_EMPTY);
        }
        return Optional.empty();
    }

    @SneakyThrows
    private String convertField(Field field, Object obj) {
        field.setAccessible(true);
        return STRING_QUOT + field.getName() + STRING_QUOT + JSON_FIELD_SEPARATOR + serialize(field.get(obj));
    }

    @SneakyThrows
    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            Field field = clazz.getDeclaredField(entry.getKey());
            field.setAccessible(true);

            if(entry.getValue().isJsonArray()) {
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> listClassType = (Class<?>) listType.getActualTypeArguments()[0];
                field.set(instance, getArrayValue(
                        entry.getValue().getAsJsonArray(),
                        field.getType(),
                        listClassType)
                );
            } else {
                field.set(instance, getValue(entry.getValue(), field.getType()));
            }
        }
        return instance;
    }
    private Object getValue(JsonElement value, Class<?> type) {
        if (value.isJsonNull()) {
            return null;
        } else if (value.isJsonPrimitive()) {
            return getPrimitiveValue(value.getAsJsonPrimitive(), type);
        } else {
            return deserialize(value.toString(), type);
        }
    }
    private Object getPrimitiveValue(JsonElement value, Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return value.getAsInt();
        } else if (type == long.class || type == Long.class) {
            return value.getAsLong();
        } else if (type == float.class || type == Float.class) {
            return value.getAsFloat();
        } else if (type == double.class || type == Double.class) {
            return value.getAsDouble();
        } else if (type == boolean.class || type == Boolean.class) {
            return value.getAsBoolean();
        } else if (type == String.class) {
            return value.getAsString();
        } else {
            throw new IllegalArgumentException("Unsupported primitive type: " + type.getName());
        }
    }

    private Object getArrayValue(JsonArray value, Class<?> fieldClass, Class<?> fieldGenericClass) {
        if (fieldClass.isArray()) {
            return listOfObject(value,fieldGenericClass)
                    .toArray();
        } else if (List.class.isAssignableFrom(fieldClass)) {
            return listOfObject(value, fieldGenericClass);
        } else {
            throw new IllegalArgumentException("Unsupported array type: " + fieldClass.getName());
        }
    }

    private List<Object> listOfObject(JsonArray value, Class<?> componentType) {
        return value.asList()
                .stream()
                .map(element -> getValue(element, componentType))
                .toList();
    }
}
