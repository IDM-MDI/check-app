package ru.clevertec.test.checkapp.util.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.math.BigDecimal;
import java.util.List;

class ProductModelMapperTest {
    private static final ProductModelMapper PRODUCT_MODEL_MAPPER = new ProductModelMapper();
    private static final Product ENTITY = Product.builder()
            .id(1L)
            .name("test")
            .price(BigDecimal.valueOf(1.0))
            .build();
    private static final ProductModel MODEL = ProductModel.builder()
            .id(1L)
            .name("test")
            .price(1.0)
            .build();
    @Test
    void toEntityShouldBeCorrect() {
        Product actual = PRODUCT_MODEL_MAPPER.toEntity(MODEL);
        Assertions.assertEquals(ENTITY,actual);
    }

    @Test
    void toModelShouldBeCorrect() {
        ProductModel actual = PRODUCT_MODEL_MAPPER.toModel(ENTITY);
        Assertions.assertEquals(MODEL,actual);
    }

    @Test
    void toEntityListShouldBeCorrect() {
        List<Product> expected = List.of(ENTITY);
        List<Product> actual = PRODUCT_MODEL_MAPPER.toEntityList(List.of(MODEL));
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void toModelListShouldBeCorrect() {
        List<ProductModel> expected = List.of(MODEL);
        List<ProductModel> actual = PRODUCT_MODEL_MAPPER.toModelList(List.of(ENTITY));
        Assertions.assertEquals(expected,actual);
    }
    @Test
    void toEntityShouldBeNull() {
        Assertions.assertNull(PRODUCT_MODEL_MAPPER.toEntity(null));
    }

    @Test
    void toModelShouldBeNull() {
        Assertions.assertNull(PRODUCT_MODEL_MAPPER.toModel(null));
    }

    @Test
    void toEntityListShouldBeEmpty() {
        Assertions.assertTrue(PRODUCT_MODEL_MAPPER.toEntityList(null)
                .isEmpty());
    }

    @Test
    void toModelListShouldBeNull() {
        Assertions.assertTrue(PRODUCT_MODEL_MAPPER.toModelList(null)
                .isEmpty());
    }
}