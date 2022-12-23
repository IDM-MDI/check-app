package ru.clevertec.test.checkapp.validator;

import ru.clevertec.test.checkapp.model.ProductModel;

import java.util.Objects;

public class ProductValidator {
    private ProductValidator(){}

    public static boolean isProductValid(ProductModel model) {
        return Objects.nonNull(model) &&
                isNameValid(model.getName()) &&
                isPriceValid(model.getPrice());
    }

    public static boolean isPriceValid(double price) {
        return price > 0;
    }

    public static boolean isNameValid(String name) {
        return Objects.nonNull(name) &&
                !name.isBlank() &&
                name.length() > 1 && name.length() < 255;
    }
}
