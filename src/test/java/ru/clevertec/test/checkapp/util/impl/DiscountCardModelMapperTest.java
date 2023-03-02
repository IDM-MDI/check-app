package ru.clevertec.test.checkapp.util.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.model.DiscountCardModel;

import java.util.List;

class DiscountCardModelMapperTest {
    static final DiscountCardModelMapper DISCOUNT_MODEL_MAPPER = new DiscountCardModelMapper();
    static final DiscountCard ENTITY = DiscountCard.builder()
            .id(1L)
            .number(1234)
            .discount(10)
            .build();
    static final DiscountCardModel MODEL = DiscountCardModel.builder()
            .id(1L)
            .number(1234)
            .discount(10)
            .build();
    @Test
    void toEntityShouldBeCorrect() {
        DiscountCard actual = DISCOUNT_MODEL_MAPPER.toEntity(MODEL);
        Assertions.assertEquals(ENTITY,actual);
    }

    @Test
    void toModelShouldBeCorrect() {
        DiscountCardModel actual = DISCOUNT_MODEL_MAPPER.toModel(ENTITY);
        Assertions.assertEquals(MODEL,actual);
    }

    @Test
    void toEntityListShouldBeCorrect() {
        List<DiscountCard> expected = List.of(ENTITY);
        List<DiscountCard> actual = DISCOUNT_MODEL_MAPPER.toEntityList(List.of(MODEL));
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void toModelListShouldBeCorrect() {
        List<DiscountCardModel> expected = List.of(MODEL);
        List<DiscountCardModel> actual = DISCOUNT_MODEL_MAPPER.toModelList(List.of(ENTITY));
        Assertions.assertEquals(expected,actual);
    }
    @Test
    void toEntityShouldBeNull() {
        Assertions.assertNull(DISCOUNT_MODEL_MAPPER.toEntity(null));
    }

    @Test
    void toModelShouldBeNull() {
        Assertions.assertNull(DISCOUNT_MODEL_MAPPER.toModel(null));
    }

    @Test
    void toEntityListShouldBeEmpty() {
        Assertions.assertTrue(DISCOUNT_MODEL_MAPPER.toEntityList(null)
                .isEmpty());
    }

    @Test
    void toModelListShouldBeNull() {
        Assertions.assertTrue(DISCOUNT_MODEL_MAPPER.toModelList(null)
                .isEmpty());
    }
}