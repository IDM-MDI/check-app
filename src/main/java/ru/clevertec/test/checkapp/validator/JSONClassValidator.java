package ru.clevertec.test.checkapp.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSONClassValidator {
    public static boolean isPrimitive(Object obj) {
        return isNumber(obj) || isText(obj) || isBoolean(obj);
    }
    public static boolean isText(Object obj) {
        return obj.getClass() == String.class || obj.getClass() == Character.class;
    }
    public static boolean isNumber(Object obj) {
        Class<?> objClass = obj.getClass();
        return objClass == Byte.class ||
                objClass == Short.class ||
                objClass == Integer.class ||
                objClass == Long.class ||
                objClass == Float.class ||
                objClass == Double.class;
    }
    public static boolean isBoolean(Object obj) {
        return obj.getClass() == Boolean.class;
    }
}
