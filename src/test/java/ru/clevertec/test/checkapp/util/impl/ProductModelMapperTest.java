package ru.clevertec.test.checkapp.util.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.math.BigDecimal;
import java.util.List;

class ProductModelMapperTest {
    static final ProductModelMapper PRODUCT_MODEL_MAPPER = new ProductModelMapper();
    static final Product ENTITY = Product.builder()
            .id(1L)
            .name("test")
            .price(BigDecimal.valueOf(1.0))
            .build();
    static final ProductModel MODEL = ProductModel.builder()
            .id(1L)
            .name("test")
            .price(1.0)
            .build();
    @Test
    void toEntityShouldBeCorrect() {
        Product actual = PRODUCT_MODEL_MAPPER.toEntity(MODEL);
        Assertions.assertThat(actual).isEqualTo(ENTITY);
    }

    @Test
    void toModelShouldBeCorrect() {
        ProductModel actual = PRODUCT_MODEL_MAPPER.toModel(ENTITY);
        Assertions.assertThat(actual).isEqualTo(MODEL);
    }

    @Test
    void toEntityListShouldBeCorrect() {
        List<Product> expected = List.of(ENTITY);
        List<Product> actual = PRODUCT_MODEL_MAPPER.toEntityList(List.of(MODEL));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toModelListShouldBeCorrect() {
        List<ProductModel> expected = List.of(MODEL);
        List<ProductModel> actual = PRODUCT_MODEL_MAPPER.toModelList(List.of(ENTITY));
        Assertions.assertThat(actual).isEqualTo(expected);
    }
    @Test
    void toEntityShouldBeNull() {
        Assertions.assertThat(PRODUCT_MODEL_MAPPER.toEntity(null)).isNull();
    }

    @Test
    void toModelShouldBeNull() {
        Assertions.assertThat(PRODUCT_MODEL_MAPPER.toModel(null)).isNull();
    }

    @Test
    void toEntityListShouldBeEmpty() {
        Assertions.assertThat(PRODUCT_MODEL_MAPPER.toEntityList(null)).isEmpty();
    }

    @Test
    void toModelListShouldBeNull() {
        Assertions.assertThat(PRODUCT_MODEL_MAPPER.toModelList(null)).isEmpty();
    }
}