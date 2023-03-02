package ru.clevertec.test.checkapp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.test.checkapp.config.RepositoryConfig;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.model.ProductModel;
import ru.clevertec.test.checkapp.util.impl.ProductModelMapper;

@DataJpaTest
@ContextConfiguration(classes = RepositoryConfig.class)
@ComponentScan(basePackages = "ru.clevertec.test")
class ProductRepositoryTest {
    static final ProductModelMapper MODEL_MAPPER = new ProductModelMapper();
    static final ProductModel PRODUCT_MODEL = ProductModel.builder()
            .name("test")
            .price(1)
            .offer(true)
            .build();
    static final Product PRODUCT_ENTITY = MODEL_MAPPER.toEntity(PRODUCT_MODEL);
    final ProductRepository repository;

    @Autowired
    ProductRepositoryTest(ProductRepository repository) {
        this.repository = repository;
    }

    @Test
    void existsProductByNameShouldBeTrue() {
        repository.save(PRODUCT_ENTITY);
        Assertions.assertTrue(repository.existsProductByName(PRODUCT_ENTITY.getName()));
    }
    @Test
    void existsProductByNameShouldBeFalse() {
        Assertions.assertFalse(repository.existsProductByName(PRODUCT_ENTITY.getName()));
    }
}