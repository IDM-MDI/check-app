package ru.clevertec.test.checkapp.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.model.ProductModel;

class ProductValidatorTest {
    @Nested
    class IsProductValid {
        @Test
        void isProductValidShouldBeTrue() {
            ProductModel test = ProductModel.builder()
                    .name("test")
                    .price(100)
                    .build();
            Assertions.assertThat(ProductValidator.isProductValid(test)).isTrue();
        }
        @Test
        void isProductValidShouldBeFalse() {
            Assertions.assertThat(ProductValidator.isProductValid(null)).isFalse();
        }
    }

    @Nested
    class IsPriceValid {
        @Test
        void isPriceValidShouldBeTrue() {
            Assertions.assertThat(ProductValidator.isPriceValid(1)).isTrue();
        }

        @Test
        void isPriceValidShouldBeFalse() {
            Assertions.assertThat(ProductValidator.isPriceValid(0)).isFalse();
        }
    }

    @Nested
    class IsNameValid {
        @Test
        void isNameValidShouldBeTrue() {
            Assertions.assertThat(ProductValidator.isNameValid("test")).isTrue();
        }

        @Test
        void isNameValidShouldBeFalse() {
            Assertions.assertThat(ProductValidator.isNameValid("")).isFalse();
        }
    }
}