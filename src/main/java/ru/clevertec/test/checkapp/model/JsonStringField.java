package ru.clevertec.test.checkapp.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonStringField {
    public static final String JSON_START = "{";
    public static final String JSON_END = "}";
    public static final String JSON_ARRAY_START = "[";
    public static final String JSON_ARRAY_END = "]";
    public static final String NULL_STRING = "null";
    public static final String JSON_ARRAY_EMPTY = JSON_ARRAY_START + JSON_ARRAY_END;
    public static final String STRING_QUOT = "\"";
    public static final String CHAR_QUOT = "'";
    public static final String JSON_FIELD_SEPARATOR = ":";
}
