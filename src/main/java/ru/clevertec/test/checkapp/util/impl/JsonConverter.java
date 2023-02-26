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

/**
 * A utility class for converting Java objects to and from JSON format using the Gson library.
 * This class implements the {@link JsonSerializer} and {@link JsonDeserializer} interfaces to provide custom serialization and deserialization logic.
 * @author Dayanch
 */
@Component
@RequiredArgsConstructor
public class JsonConverter implements JsonSerializer, JsonDeserializer {
    private final JsonSerializeHandler handler;
    private final Gson gson;

    /**
     * Serializes a single object to JSON format.
     * @param obj the object to serialize
     * @return a string representation of the serialized object
     * @throws HttpMediaTypeNotAcceptableException if there are no acceptable representations of the object
     */
    @SneakyThrows
    public String serialize(Object obj) {
        return objectSerializeValidChecker(obj).orElseGet(() -> serializeObject(obj));
    }


    /**
     * Serializes an array of objects to JSON format.
     * @param objects the objects to serialize
     * @return a string representation of the serialized objects
     * @throws HttpMediaTypeNotAcceptableException if there are no acceptable representations of the objects
     */
    public String serialize(Object[] objects) {
        if(Objects.isNull(objects)) {
            return NULL_STRING;
        }
        return serialize(Arrays.stream(objects).toList());
    }


    /**
     * Serializes a list of objects to JSON format.
     * @param objects the objects to serialize
     * @return a string representation of the serialized objects
     * @throws HttpMediaTypeNotAcceptableException if there are no acceptable representations of the objects
     */
    public String serialize(List<?> objects) {
        return listSerializeValidChecker(objects)
                .orElseGet(() -> JSON_ARRAY_START + handler.joinToString(objects, this::serialize) + JSON_ARRAY_END);
    }


    /**
     * Deserializes a JSON string to an object of the specified class.
     * @param json the JSON string to deserialize
     * @param clazz the class of the object to deserialize to
     * @param <T> the type of the object to deserialize to
     * @return an object of the specified class deserialized from the JSON string
     */
    @SneakyThrows
    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        T instance = clazz.getDeclaredConstructor().newInstance();
        jsonObject.entrySet().forEach(element -> setValueToField(clazz,instance,element));
        return instance;
    }


    /**
     * Serializes a single object to JSON format.
     * @param obj the object to serialize
     * @return a string representation of the serialized object
     */
    @SneakyThrows
    private String serializeObject(Object obj) {
        List<Field> fields = Arrays.stream(obj.getClass().getDeclaredFields()).toList();
        if(fields.isEmpty()) {
            throw new HttpMediaTypeNotAcceptableException("No acceptable representation");
        }
        return JSON_START + handler.joinToString(fields, o -> convertField((Field) o, obj)) + JSON_END;
    }


    /**
     * Checks if the object to be serialized is valid and can be serialized to a JSON string.
     * @param obj the object to check
     * @return an optional containing the serialized object if it is valid, otherwise an empty optional
     */
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


    /**
     * Checks if a given list of objects can be serialized into a JSON array.
     *
     * @param objects the list of objects to check
     * @return an Optional containing a string error message if the list cannot be serialized, or empty if it can
     */
    private Optional<String> listSerializeValidChecker(List<?> objects) {
        if(Objects.isNull(objects)) {
            return Optional.of(NULL_STRING);
        }
        else if(objects.isEmpty()) {
            return Optional.of(JSON_ARRAY_EMPTY);
        }
        return Optional.empty();
    }


    /**
     * Converts a field of an object to a JSON field-value pair.
     *
     * @param field the field to convert
     * @param obj the object containing the field
     * @return the JSON field-value pair as a string
     * @throws IllegalAccessException if the field is not accessible
     */
    @SneakyThrows
    private String convertField(Field field, Object obj) {
        field.setAccessible(true);
        return STRING_QUOT + field.getName() + STRING_QUOT + JSON_FIELD_SEPARATOR + serialize(field.get(obj));
    }


    /**
     * Sets the value of a field in an object based on a JSON field-value pair.
     *
     * @param clazz the class of the object
     * @param instance the object instance
     * @param entry the JSON field-value pair as a Map.Entry
     * @param <T> the type of the object
     * @throws NoSuchFieldException if the field does not exist
     * @throws IllegalAccessException if the field is not accessible
     */
    @SneakyThrows
    private <T> void setValueToField(Class<T> clazz, T instance, Map.Entry<String, JsonElement> entry) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(entry.getKey());
            field.setAccessible(true);
            ParameterizedType listType = (ParameterizedType) field.getGenericType();
            Class<?> listClassType = (Class<?>) listType.getActualTypeArguments()[0];
            field.set(instance, getArrayValue(
                    entry.getValue().getAsJsonArray(),
                    field.getType(),
                    listClassType)
            );
        } catch (ClassCastException e) {
            field.set(instance, getValue(entry.getValue(), field.getType()));
        } catch (NoSuchFieldException e) {
            return;
        }
    }


    /**
     * Gets the deserialized value of a JSON element based on its type.
     *
     * @param value the JSON element to deserialize
     * @param type the type of the element
     * @return the deserialized object
     */
    private Object getValue(JsonElement value, Class<?> type) {
        if (value.isJsonNull()) {
            return null;
        } else if (value.isJsonPrimitive()) {
            return getPrimitiveValue(value.getAsJsonPrimitive(), type);
        } else {
            return deserialize(value.toString(), type);
        }
    }

    /**
     * Converts a JSON primitive element to a Java primitive value of the specified type.
     *
     * @param value the JSON primitive element to convert
     * @param type  the target type of the conversion
     * @return the converted Java primitive value
     * @throws IllegalArgumentException if the specified type is not supported
     */
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

    /**
     * Converts a JSON array element to a Java array or list of objects of the specified types.
     *
     * @param value             the JSON array element to convert
     * @param fieldClass        the target field class of the conversion
     * @param fieldGenericClass the target generic type of the conversion (if applicable)
     * @return the converted Java array or list of objects
     * @throws IllegalArgumentException if the specified type is not supported
     */
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


    /**
     * Converts a JSON array element to a list of Java objects of the specified type.
     *
     * @param value         the JSON array element to convert
     * @param componentType the target type of the conversion
     * @return the converted list of Java objects
     * @throws IllegalArgumentException if the specified type is not supported
     */
    private List<Object> listOfObject(JsonArray value, Class<?> componentType) {
        return value.asList()
                .stream()
                .map(element -> getValue(element, componentType))
                .toList();
    }
}
