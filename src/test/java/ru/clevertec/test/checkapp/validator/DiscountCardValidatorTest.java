package ru.clevertec.test.checkapp.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.model.DiscountCardModel;

class DiscountCardValidatorTest {
    @Nested
    class IsDiscountCardValid {
        @Test
        void isDiscountCardValidShouldBeTrue() {
            DiscountCardModel card = DiscountCardModel.builder()
                    .number(1)
                    .discount(1)
                    .build();
            Assertions.assertThat(DiscountCardValidator.isDiscountCardValid(card)).isTrue();
        }

        @Test
        void isDiscountCardValidShouldBeFalse() {
            Assertions.assertThat(DiscountCardValidator.isDiscountCardValid(null)).isFalse();
        }
    }
    @Nested
    class IsNumberValid {
        @Test
        void isNumberValidShouldBeTrue() {
            Assertions.assertThat(DiscountCardValidator.isNumberValid(1)).isTrue();
        }

        @Test
        void isNumberValidShouldBeFalse() {
            Assertions.assertThat(DiscountCardValidator.isNumberValid(-1)).isFalse();
        }
    }

    @Nested
    class IsNumberZero {
        @Test
        void isNumberZeroShouldBeTrue() {
            Assertions.assertThat(DiscountCardValidator.isNumberZero(0)).isTrue();
        }

        @Test
        void isNumberZeroShouldBeFalse() {
            Assertions.assertThat(DiscountCardValidator.isNumberZero(1)).isFalse();
        }
    }

    @Nested
    class IsDiscountValid {
        @Test
        void isDiscountValidShouldBeTrue() {
            Assertions.assertThat(DiscountCardValidator.isDiscountValid(55)).isTrue();
        }
        @Test
        void isDiscountValidShouldBeFalse() {
            Assertions.assertThat(DiscountCardValidator.isDiscountValid(0)).isFalse();
        }
    }
}