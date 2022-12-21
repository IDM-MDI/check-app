package ru.clevertec.test.checkapp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestParameterUtil {
    private static final String QUERY_SEPARATOR = "&";
    private static final String QUERY_ASSIGNMENT = "=";
    private static final String DISCOUNT_CARD_NAME = "card";
    private static final String PRODUCT_NAME = "id";
    private RequestParameterUtil() {}
    public static List<Long> findProducts(String queryString) {
        String[] splitQuery = separateQuery(queryString);
        String[] queryValues = findQueryValue(PRODUCT_NAME,splitQuery);
        return Arrays.stream(queryValues)
                .map(Long::parseLong)
                .toList();
    }

    public static String[] separateQuery(String query) {
        return query.split(QUERY_SEPARATOR);
    }
    public static String[] findQueryValue(String queryName, String[] query) {
        List<String> result = new ArrayList<>();
        for (String s : query) {
            if(s.startsWith(queryName + QUERY_ASSIGNMENT)) {
                result.add(s.split(QUERY_ASSIGNMENT)[1]);
            }
        }
        return result.toArray(new String[0]);
    }
    public static int findDiscountCardNumber(String queryString) {
        String[] splitQuery = separateQuery(queryString);
        String[] query = findQueryValue(DISCOUNT_CARD_NAME, splitQuery);
        return query.length < 1 ? 0 : Integer.parseInt(query[1]);
    }
}
