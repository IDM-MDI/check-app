package ru.clevertec.test.checkapp.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JSONClassValidatorTest {

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

    @Test
    void isPrimitiveShouldShouldThrowNPE() {
        Assertions.assertThatThrownBy(() -> JSONClassValidator.isPrimitive(null))
                .isInstanceOf(NullPointerException.class);
    }

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

    @Test
    void isTextShouldShouldThrowNPE() {
        Assertions.assertThatThrownBy(() -> JSONClassValidator.isText(null))
                .isInstanceOf(NullPointerException.class);
    }

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

    @Test
    void isNumberShouldShouldThrowNPE() {
        Assertions.assertThatThrownBy(() -> JSONClassValidator.isNumber(null))
                .isInstanceOf(NullPointerException.class);
    }

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
    @Test
    void isBooleanShouldShouldThrowNPE() {
        Assertions.assertThatThrownBy(() -> JSONClassValidator.isBoolean(null))
                .isInstanceOf(NullPointerException.class);
    }
}