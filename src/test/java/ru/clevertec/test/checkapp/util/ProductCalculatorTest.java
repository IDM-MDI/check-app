package ru.clevertec.test.checkapp.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.model.CheckProduct;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.util.Set;

class ProductCalculatorTest {
    static final ProductModel PRODUCT_MODEL = ProductModel.builder()
            .id(1)
            .name("test")
            .price(1)
            .build();
    static final CheckModel CHECK_MODEL = CheckModel.builder()
            .discountCard(
                    DiscountCardModel.builder()
                            .id(1)
                            .number(1)
                            .discount(1)
                            .build()
            )
            .elements(Set.of(
                    CheckProduct.builder()
                            .product(PRODUCT_MODEL)
                            .count(1)
                            .totalPrice(1)
                            .build()
            ))
            .totalPrice(5)
            .totalPriceWithoutCard(5)
            .build();

    @Test
    void calculateCertainProduct() {
        double expected = 5.0;
        double actual = ProductCalculator.calculateCertainProduct(PRODUCT_MODEL, 5);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void calculateTotalPriceWithoutDiscount() {
        double expected = 1.0;
        double actual = ProductCalculator.calculateTotalPriceWithoutDiscount(CHECK_MODEL);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void calculateTotalPriceWithDiscount() {
        double expected = 4.95;
        double actual = ProductCalculator.calculateTotalPriceWithDiscount(CHECK_MODEL);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void calculateDiscount() {
        double expected = 150.0;
        double actual = ProductCalculator.calculateDiscount(1000, 15);
        Assertions.assertEquals(expected,actual);
    }
}