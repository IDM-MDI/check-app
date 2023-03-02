package ru.clevertec.test.checkapp.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ListValidatorTest {

    @Test
    void isListEmptyShouldBeTrue() {
        Assertions.assertThat(ListValidator.isListEmpty(List.of())).isTrue();
    }

    @Test
    void isListEmptyShouldBeFalse() {
        Assertions.assertThat(ListValidator.isListEmpty(List.of(1))).isFalse();
    }
}