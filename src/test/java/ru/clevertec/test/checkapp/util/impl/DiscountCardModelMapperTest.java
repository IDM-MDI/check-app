package ru.clevertec.test.checkapp.util.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.model.DiscountCardModel;

import java.util.List;

class DiscountCardModelMapperTest {
    private static final DiscountCardModelMapper DISCOUNT_MODEL_MAPPER = new DiscountCardModelMapper();
    private static final DiscountCard ENTITY = DiscountCard.builder()
            .id(1L)
            .number(1234)
            .discount(10)
            .build();
    private static final DiscountCardModel MODEL = DiscountCardModel.builder()
            .id(1L)
            .number(1234)
            .discount(10)
            .build();
    @Test
    void toEntityShouldBeCorrect() {
        DiscountCard actual = DISCOUNT_MODEL_MAPPER.toEntity(MODEL);
        Assertions.assertThat(actual).isEqualTo(ENTITY);
    }

    @Test
    void toModelShouldBeCorrect() {
        DiscountCardModel actual = DISCOUNT_MODEL_MAPPER.toModel(ENTITY);
        Assertions.assertThat(actual).isEqualTo(MODEL);
    }

    @Test
    void toEntityListShouldBeCorrect() {
        List<DiscountCard> expected = List.of(ENTITY);
        List<DiscountCard> actual = DISCOUNT_MODEL_MAPPER.toEntityList(List.of(MODEL));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toModelListShouldBeCorrect() {
        List<DiscountCardModel> expected = List.of(MODEL);
        List<DiscountCardModel> actual = DISCOUNT_MODEL_MAPPER.toModelList(List.of(ENTITY));
        Assertions.assertThat(actual).isEqualTo(expected);
    }
    @Test
    void toEntityShouldBeNull() {
        Assertions.assertThat(DISCOUNT_MODEL_MAPPER.toEntity(null)).isNull();
    }

    @Test
    void toModelShouldBeNull() {
        Assertions.assertThat(DISCOUNT_MODEL_MAPPER.toModel(null)).isNull();
    }

    @Test
    void toEntityListShouldBeEmpty() {
        Assertions.assertThat(DISCOUNT_MODEL_MAPPER.toEntityList(null)).isEmpty();
    }

    @Test
    void toModelListShouldBeNull() {
        Assertions.assertThat(DISCOUNT_MODEL_MAPPER.toModelList(null)).isEmpty();
    }
}