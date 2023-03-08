package ru.clevertec.test.checkapp.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.test.checkapp.config.RepositoryConfig;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.model.ProductModel;
import ru.clevertec.test.checkapp.util.impl.ProductModelMapper;

@DataJpaTest
@ContextConfiguration(classes = RepositoryConfig.class)
@ComponentScan(basePackages = "ru.clevertec.test")
@ActiveProfiles("test")
@RequiredArgsConstructor
class ProductRepositoryTest {
    private static final ProductModelMapper MODEL_MAPPER = new ProductModelMapper();
    private static final ProductModel PRODUCT_MODEL = ProductModel.builder()
            .name("test")
            .price(1)
            .offer(true)
            .build();
    private static final Product PRODUCT_ENTITY = MODEL_MAPPER.toEntity(PRODUCT_MODEL);
    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        repository.save(PRODUCT_ENTITY);
    }

    @AfterEach
    void deleteAll() {
        repository.deleteAll();
    }

    @Nested
    class ExistProductByName {
        @Test
        void existsProductByNameShouldBeTrue() {
            boolean result = repository.existsProductByName(PRODUCT_ENTITY.getName());
            Assertions.assertThat(result).isTrue();
        }
        @Test
        void existsProductByNameShouldBeFalse() {
            String empty = "";
            boolean result = repository.existsProductByName(empty);
            Assertions.assertThat(result).isFalse();
        }
    }
}