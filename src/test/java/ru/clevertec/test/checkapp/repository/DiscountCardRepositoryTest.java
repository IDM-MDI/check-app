package ru.clevertec.test.checkapp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.test.checkapp.config.RepositoryConfig;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.util.impl.DiscountCardModelMapper;

import java.util.Optional;

@DataJpaTest
@ContextConfiguration(classes = RepositoryConfig.class)
@ComponentScan(basePackages = "ru.clevertec.test")
class DiscountCardRepositoryTest {
    private static final DiscountCardModelMapper MODEL_MAPPER = new DiscountCardModelMapper();
    private static final DiscountCardModel MODEL_CARD = DiscountCardModel.builder()
            .number(1)
            .discount(1)
            .build();
    private static final DiscountCard ENTITY_CARD = MODEL_MAPPER.toEntity(MODEL_CARD);
    private final DiscountCardRepository repository;
    @Autowired
    DiscountCardRepositoryTest(DiscountCardRepository repository) {
        this.repository = repository;
    }

    @Test
    void existsDiscountCardByNumberShouldBeTrue() {
        repository.save(ENTITY_CARD);
        Assertions.assertTrue(repository.existsDiscountCardByNumber(ENTITY_CARD.getNumber()));
    }

    @Test
    void existsDiscountCardByNumberShouldBeFalse() {
        Assertions.assertFalse(repository.existsDiscountCardByNumber(ENTITY_CARD.getNumber()));
    }
    @Test
    void findDiscountCardByNumber() {
        repository.save(ENTITY_CARD);
        DiscountCard actual = repository.findDiscountCardByNumber(ENTITY_CARD.getNumber());
        DiscountCard expected = MODEL_MAPPER.toEntity(MODEL_CARD);
        expected.setId(1L);
        Assertions.assertEquals(expected,actual);
    }
}