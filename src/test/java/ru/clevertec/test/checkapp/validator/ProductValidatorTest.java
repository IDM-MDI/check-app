package ru.clevertec.test.checkapp.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.model.ProductModel;

class ProductValidatorTest {

    @Test
    void isProductValidShouldBeTrue() {
        ProductModel test = ProductModel.builder()
                .name("test")
                .price(100)
                .build();
        Assertions.assertTrue(ProductValidator.isProductValid(test));
    }
    @Test
    void isProductValidShouldBeFalse() {
        Assertions.assertFalse(ProductValidator.isProductValid(null));
    }
    @Test
    void isPriceValidShouldBeTrue() {
        Assertions.assertTrue(ProductValidator.isPriceValid(1));
    }

    @Test
    void isPriceValidShouldBeFalse() {
        Assertions.assertFalse(ProductValidator.isPriceValid(0));
    }
    @Test
    void isNameValidShouldBeTrue() {
        Assertions.assertTrue(ProductValidator.isNameValid("test"));
    }

    @Test
    void isNameValidShouldBeFalse() {
        Assertions.assertFalse(ProductValidator.isNameValid(""));
    }
}