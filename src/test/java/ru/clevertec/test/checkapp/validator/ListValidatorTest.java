package ru.clevertec.test.checkapp.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ListValidatorTest {

    @Test
    void isListEmptyShouldBeTrue() {
        Assertions.assertTrue(ListValidator.isListEmpty(List.of()));
    }

    @Test
    void isListEmptyShouldBeFalse() {
        Assertions.assertFalse(ListValidator.isListEmpty(List.of(1)));
    }
}