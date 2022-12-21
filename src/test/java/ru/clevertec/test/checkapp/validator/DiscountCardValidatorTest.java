package ru.clevertec.test.checkapp.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.model.DiscountCardModel;

class DiscountCardValidatorTest {

    @Test
    void isDiscountCardValidShouldBeTrue() {
        DiscountCardModel card = DiscountCardModel.builder()
                .number(1)
                .discount(1)
                .build();
        Assertions.assertTrue(DiscountCardValidator.isDiscountCardValid(card));
    }

    @Test
    void isDiscountCardValidShouldBeFalse() {
        Assertions.assertFalse(DiscountCardValidator.isDiscountCardValid(null));
    }

    @Test
    void isNumberValidShouldBeTrue() {
        Assertions.assertTrue(DiscountCardValidator.isNumberValid(1));
    }

    @Test
    void isNumberValidShouldBeFalse() {
        Assertions.assertFalse(DiscountCardValidator.isNumberValid(-1));
    }

    @Test
    void isNumberZeroShouldBeTrue() {
        Assertions.assertTrue(DiscountCardValidator.isNumberZero(0));
    }

    @Test
    void isNumberZeroShouldBeFalse() {
        Assertions.assertFalse(DiscountCardValidator.isNumberZero(1));
    }

    @Test
    void isDiscountValidShouldBeTrue() {
        Assertions.assertTrue(DiscountCardValidator.isDiscountValid(55));
    }
    @Test
    void isDiscountValidShouldBeFalse() {
        Assertions.assertFalse(DiscountCardValidator.isDiscountValid(0));
    }
}