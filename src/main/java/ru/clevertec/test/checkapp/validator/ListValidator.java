package ru.clevertec.test.checkapp.validator;

import java.util.List;
import java.util.Objects;

public class ListValidator {
    private ListValidator() {}

    public static boolean isListEmpty(List<?> list) {
        return Objects.isNull(list) || list.isEmpty();
    }
}
