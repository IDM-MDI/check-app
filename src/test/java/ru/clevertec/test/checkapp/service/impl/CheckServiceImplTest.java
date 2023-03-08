package ru.clevertec.test.checkapp.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.test.checkapp.builder.impl.DiscountCardBuilder;
import ru.clevertec.test.checkapp.builder.impl.ProductBuilder;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static ru.clevertec.test.checkapp.builder.impl.CheckBuilder.aCheck;
import static ru.clevertec.test.checkapp.builder.impl.CheckProductBuilder.aCheckProduct;
import static ru.clevertec.test.checkapp.builder.impl.ProductBuilder.aProduct;

@ExtendWith(MockitoExtension.class)
class CheckServiceImplTest {
    private static final String QUERY = "id=1&id=2&id=3&card=1234";
    private static final int CARD_NUMBER = 1234;
    @Mock
    private ProductServiceImpl productService;
    @Mock
    private DiscountCardServiceImpl discountCardService;
    @InjectMocks
    private CheckServiceImpl service;

    private static final List<Long> IDS = List.of(1L,2L,3L);
    private List<ProductModel> products;
    private DiscountCardModel discountCard;

    private CheckModel checkModel;

    @BeforeEach
    void setup() {
        products = List.of(
                aProduct()
                        .withId(1L)
                        .withPrice(new BigDecimal(1))
                        .withOnOffer(false)
                        .buildToModel(),
                aProduct()
                        .withId(2L)
                        .withPrice(new BigDecimal(2))
                        .withOnOffer(false)
                        .buildToModel(),
                aProduct()
                        .withId(3L)
                        .withPrice(new BigDecimal(3))
                        .withOnOffer(false)
                        .buildToModel()
        );
        discountCard = DiscountCardBuilder.aDiscountCard()
                .withDiscount(1234)
                .withDiscount(15)
                .buildToModel();

        checkModel = aCheck()
                .withElements(Set.of(
                        aCheckProduct().withProduct(products.get(0)).withTotalPrice(1).buildToModel(),
                        aCheckProduct().withProduct(products.get(1)).withTotalPrice(2).buildToModel(),
                        aCheckProduct().withProduct(products.get(2)).withTotalPrice(3).buildToModel()))
                .withDiscountCard(discountCard)
                .withTotalPrice(5.1)
                .withTotalPriceWithoutCard(6.0)
                .buildToModel();
    }
    @Test
    void getCheckShouldReturnCorrectCheck() throws ServiceException {
        doReturn(products).when(productService).findByID(IDS);
        doReturn(discountCard).when(discountCardService).findByNumber(CARD_NUMBER);

        CheckModel actual = service.getCheck(QUERY);
        actual.setCreatedTime(null);

        Assertions.assertThat(actual).isEqualTo(checkModel);
    }
}