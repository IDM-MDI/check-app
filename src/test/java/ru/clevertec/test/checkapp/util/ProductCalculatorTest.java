package ru.clevertec.test.checkapp.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.model.CheckProduct;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.util.Set;

class ProductCalculatorTest {
    private ProductModel productModel;
    private CheckModel checkModel;
    @BeforeEach
    void setup() {
        productModel = ProductModel.builder()
                .id(1)
                .name("test")
                .price(1)
                .build();
        checkModel = CheckModel.builder()
                .discountCard(
                        DiscountCardModel.builder()
                                .id(1)
                                .number(1)
                                .discount(1)
                                .build()
                )
                .elements(Set.of(
                        CheckProduct.builder()
                                .product(productModel)
                                .count(1)
                                .totalPrice(1)
                                .build()
                ))
                .totalPrice(5)
                .totalPriceWithoutCard(5)
                .build();
    }
    @Test
    void calculateCertainProductShouldReturnCorrectValue() {
        double expected = 5.0;
        double actual = ProductCalculator.calculateCertainProduct(productModel, 5);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void calculateTotalPriceWithoutDiscountShouldReturnCorrectValue() {
        double expected = 1.0;
        double actual = ProductCalculator.calculateTotalPriceWithoutDiscount(checkModel);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void calculateTotalPriceWithDiscountShouldReturnCorrectValue() {
        double expected = 4.95;
        double actual = ProductCalculator.calculateTotalPriceWithDiscount(checkModel);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void calculateDiscountShouldReturnCorrectValue() {
        double expected = 150.0;
        double actual = ProductCalculator.calculateDiscount(1000, 15);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}