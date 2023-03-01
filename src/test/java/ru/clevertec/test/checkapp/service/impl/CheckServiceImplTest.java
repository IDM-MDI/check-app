package ru.clevertec.test.checkapp.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CheckServiceImplTest {
    static final String QUERY = "id=1&id=2&id=3&card=1234";
    static final int CARD_NUMBER = 1234;
    @Mock
    ProductServiceImpl productService;
    @Mock
    DiscountCardServiceImpl discountCardService;
    @InjectMocks
    CheckServiceImpl service;

    List<Long> ids;
    List<ProductModel> products;
    DiscountCardModel discountCard;

    CheckModel checkModel;

    @BeforeEach
    void setup() {
        ids = List.of(1L,2L,3L);
        products = List.of(
                ProductModel.builder().id(1).name("test1").price(1).build(),
                ProductModel.builder().id(2).name("test2").price(2).build(),
                ProductModel.builder().id(3).name("test3").price(3).build()
        );
        discountCard = DiscountCardModel.builder()
                .number(1234)
                .discount(15)
                .build();

        checkModel = CheckModel.builder()
                .elements(Set.of(
                        CheckProduct.builder()
                                .product(products.get(0))
                                .count(1)
                                .totalPrice(1)
                                .build(),
                        CheckProduct.builder()
                                .product(products.get(1))
                                .count(1)
                                .totalPrice(2)
                                .build(),
                        CheckProduct.builder()
                                .product(products.get(2))
                                .count(1)
                                .totalPrice(3)
                                .build()
                ))
                .discountCard(discountCard)
                .totalPrice(5.1)
                .totalPriceWithoutCard(6.0)
                .build();
    }
    @Test
    void getCheckShouldReturnCorrectCheck() throws ServiceException {
        doReturn(products).when(productService).findByID(ids);
        doReturn(discountCard).when(discountCardService).findByNumber(CARD_NUMBER);

        CheckModel actual = service.getCheck(QUERY);
        actual.setCreatedTime(null);

        Assertions.assertThat(actual).isEqualTo(checkModel);
    }
}