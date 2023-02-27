package ru.clevertec.test.checkapp.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.model.CheckProduct;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckServiceImplTest {
    private static final String QUERY = "id=1&id=2&id=3&card=1234";
    private static final List<Long> ID_LIST = List.of(1L,2L,3L);
    private static final int CARD_NUMBER = 1234;
    private static final List<ProductModel> PRODUCTS = List.of(
            ProductModel.builder().id(1).name("test1").price(1).build(),
            ProductModel.builder().id(2).name("test2").price(2).build(),
            ProductModel.builder().id(3).name("test3").price(3).build()
    );
    private static final DiscountCardModel DISCOUNT_CARD = DiscountCardModel.builder()
            .number(1234)
            .discount(15)
            .build();
    @Mock
    private ProductServiceImpl productService;
    @Mock
    private DiscountCardServiceImpl discountCardService;
    @InjectMocks
    private CheckServiceImpl service;


    @Test
    void getCheckShouldBeCorrect() throws ServiceException {
        CheckModel expected = CheckModel.builder()
                .elements(Set.of(
                        CheckProduct.builder()
                                .product(PRODUCTS.get(0))
                                .count(1)
                                .totalPrice(1)
                                .build(),
                        CheckProduct.builder()
                                .product(PRODUCTS.get(1))
                                .count(1)
                                .totalPrice(2)
                                .build(),
                        CheckProduct.builder()
                                .product(PRODUCTS.get(2))
                                .count(1)
                                .totalPrice(3)
                                .build()
                ))
                .discountCard(DISCOUNT_CARD)
                .totalPrice(5.1)
                .totalPriceWithoutCard(6.0)
                .build();

        when(productService.findByID(ID_LIST))
                .thenReturn(PRODUCTS);
        when(discountCardService.findByNumber(CARD_NUMBER))
                .thenReturn(DISCOUNT_CARD);

        CheckModel actual = service.getCheck(QUERY);
        actual.setCreatedTime(null);
        Assertions.assertEquals(expected,actual);
    }
}