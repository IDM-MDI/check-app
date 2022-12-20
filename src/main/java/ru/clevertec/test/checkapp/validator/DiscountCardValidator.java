package ru.clevertec.test.checkapp.validator;

import ru.clevertec.test.checkapp.model.DiscountCardModel;

import java.util.Objects;

public class DiscountCardValidator {
    private DiscountCardValidator() {}

    public static boolean isDiscountCardValid(DiscountCardModel model) {
        return Objects.nonNull(model) && isNumberValid(model.getNumber())
                && isDiscountValid(model.getDiscount());
    }
    public static boolean isNumberValid(int number) {
        return number > 0;
    }
    public static boolean isDiscountValid(int discount) {
        return discount > 0 && discount <= 100;
    }
}
