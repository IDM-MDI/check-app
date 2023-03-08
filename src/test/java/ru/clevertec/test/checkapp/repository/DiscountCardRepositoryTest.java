package ru.clevertec.test.checkapp.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.test.checkapp.config.RepositoryConfig;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.util.impl.DiscountCardModelMapper;

@DataJpaTest
@ContextConfiguration(classes = RepositoryConfig.class)
@ComponentScan(basePackages = "ru.clevertec.test")
@ActiveProfiles("test")
class DiscountCardRepositoryTest {
    private static final DiscountCardModelMapper MODEL_MAPPER = new DiscountCardModelMapper();
    private static final DiscountCardModel MODEL_CARD = DiscountCardModel.builder()
            .number(1)
            .discount(1)
            .build();
    private static final DiscountCard ENTITY_CARD = MODEL_MAPPER.toEntity(MODEL_CARD);
    @Autowired
    private DiscountCardRepository repository;


    @BeforeEach
    void setup() {
        repository.save(ENTITY_CARD);
    }

    @Nested
    class ExistByCardNumber {
        @Test
        void existsDiscountCardByNumberShouldBeTrue() {
            boolean result = repository.existsDiscountCardByNumber(ENTITY_CARD.getNumber());
            Assertions.assertThat(result).isTrue();
        }

        @Test
        void existsDiscountCardByNumberShouldBeFalse() {
            boolean result = repository.existsDiscountCardByNumber(0);
            Assertions.assertThat(result)
                    .isFalse();
        }
    }
    @Test
    void findDiscountCardByNumberShouldReturnCorrectDiscount() {
        DiscountCard actual = repository.findDiscountCardByNumber(ENTITY_CARD.getNumber());
        Assertions.assertThat(actual.getDiscount()).isEqualTo(ENTITY_CARD.getDiscount());
    }
}