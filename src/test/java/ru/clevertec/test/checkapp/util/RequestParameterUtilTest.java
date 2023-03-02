package ru.clevertec.test.checkapp.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class RequestParameterUtilTest {
    static final String TEST_QUERY = "id=1&id=2&id=3&card=1234";
    static final String[] TEST_ARRAY = List.of("id=1","id=2","id=3","card=1234")
            .toArray(new String[0]);
    @Test
    void findProductsShouldReturnCorrectValue() {
        List<Long> expected = List.of(1L,2L,3L);
        List<Long> actual = RequestParameterUtil.findProducts(TEST_QUERY);
        Assertions.assertThat(actual).containsAll(expected);
    }

    @Test
    void separateQueryShouldReturnCorrectValue() {
        String[] actual = RequestParameterUtil
                .separateQuery(TEST_QUERY);
        Assertions.assertThat(actual).containsExactly(TEST_ARRAY);
    }

    @Test
    void findQueryValueShouldReturnCorrectValue() {
        String expected = "1234";
        String[] strings = RequestParameterUtil
                .separateQuery(TEST_QUERY);
        String[] values = RequestParameterUtil.findQueryValue("card", strings);
        Assertions.assertThat(values[0]).isEqualTo(expected);
    }

    @Test
    void findDiscountCardNumberShouldReturnCorrectValue() {
        int expected = 1234;
        int actual = RequestParameterUtil.findDiscountCardNumber(TEST_QUERY);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findDiscountCardNumberShouldReturnZero() {
        int expected = 0;
        int actual = RequestParameterUtil.findDiscountCardNumber("");
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}