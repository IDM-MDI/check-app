package ru.clevertec.test.checkapp.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

class ListValidatorTest {

    @Nested
    class IsListEmpty {
        @Test
        void isListEmptyShouldBeTrue() {
            Assertions.assertThat(ListValidator.isListEmpty(List.of())).isTrue();
        }

        @Test
        void isListEmptyShouldBeFalse() {
            Assertions.assertThat(ListValidator.isListEmpty(List.of(1))).isFalse();
        }
    }
}