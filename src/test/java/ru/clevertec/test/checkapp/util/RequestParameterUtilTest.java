package ru.clevertec.test.checkapp.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class RequestParameterUtilTest {
    private static final String TEST_QUERY = "id=1&id=2&id=3&card=1234";
    private static final String[] TEST_ARRAY = List.of("id=1","id=2","id=3","card=1234")
            .toArray(new String[0]);
    @Test
    void findProducts() {
        List<Long> expected = List.of(1L,2L,3L);
        List<Long> actual = RequestParameterUtil.findProducts(TEST_QUERY);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void separateQuery() {
        String[] actual = RequestParameterUtil
                .separateQuery(TEST_QUERY);
        Assertions.assertArrayEquals(TEST_ARRAY,actual);
    }

    @Test
    void findQueryValue() {
        String expected = "1234";
        String[] strings = RequestParameterUtil
                .separateQuery(TEST_QUERY);
        String[] values = RequestParameterUtil.findQueryValue("card", strings);
        Assertions.assertEquals(expected, values[0]);
    }

    @Test
    void findDiscountCardNumber() {
        int expected = 1234;
        int actual = RequestParameterUtil.findDiscountCardNumber(TEST_QUERY);
        Assertions.assertEquals(expected,actual);
    }
}