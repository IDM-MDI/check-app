package ru.clevertec.test.checkapp.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JSONClassValidatorTest {
    @Nested
    class IsPrimitive {
        @Test
        void isPrimitiveShouldReturnTrue() {
            Assertions.assertThat(JSONClassValidator.isPrimitive(1))
                    .isTrue();
        }

        @Test
        void isPrimitiveShouldReturnFalse() {
            Assertions.assertThat(JSONClassValidator.isPrimitive(new Object()))
                    .isFalse();
        }
    }

    @Nested
    class IsText {
        @Test
        void isTextShouldReturnTrue() {
            Assertions.assertThat(JSONClassValidator.isText("text"))
                    .isTrue();
        }

        @Test
        void isTextShouldReturnFalse() {
            Assertions.assertThat(JSONClassValidator.isText(123))
                    .isFalse();
        }
    }

    @Nested
    class IsNumber {
        @Test
        void isNumberShouldReturnTrue() {
            Assertions.assertThat(JSONClassValidator.isNumber(123))
                    .isTrue();
        }

        @Test
        void isNumberShouldReturnFalse() {
            Assertions.assertThat(JSONClassValidator.isNumber("123"))
                    .isFalse();
        }
    }

    @Nested
    class IsBoolean {
        @Test
        void isBooleanShouldReturnTrue() {
            Assertions.assertThat(JSONClassValidator.isBoolean(true))
                    .isTrue();
        }

        @Test
        void isBooleanShouldReturnFalse() {
            Assertions.assertThat(JSONClassValidator.isBoolean(123))
                    .isFalse();
        }
    }
}