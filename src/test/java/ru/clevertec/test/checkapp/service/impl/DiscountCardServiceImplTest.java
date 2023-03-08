package ru.clevertec.test.checkapp.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.repository.DiscountCardRepository;
import ru.clevertec.test.checkapp.util.impl.DiscountCardModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static ru.clevertec.test.checkapp.builder.impl.DiscountCardBuilder.aDiscountCard;

@ExtendWith(MockitoExtension.class)
class DiscountCardServiceImplTest {
    private static final DiscountCard ENTITY_CARD = aDiscountCard()
            .withId(1L)
            .buildToEntity();
    private static final DiscountCardModel MODEL_CARD = aDiscountCard()
            .withId(1L)
            .buildToModel();
    @Mock
    private DiscountCardRepository mockRepository;
    @Mock
    private DiscountCardModelMapper mockModelMapper;
    @InjectMocks
    private DiscountCardServiceImpl service;


    @Nested
    class FindByID {
        @Test
        void findByIDShouldReturnCorrectCard() throws ServiceException {
            doReturn(Optional.of(ENTITY_CARD)).when(mockRepository).findById(ENTITY_CARD.getId());
            doReturn(MODEL_CARD).when(mockModelMapper).toModel(ENTITY_CARD);

            DiscountCardModel actual = service.findByID(ENTITY_CARD.getId());

            Assertions.assertThat(actual).isEqualTo(MODEL_CARD);
        }
        @Test
        void findByIDShouldThrowException() {
            Assertions.assertThatThrownBy(() -> service.findByID(ENTITY_CARD.getId()))
                    .isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    class FindByNumber {
        @Test
        void findByNumberShouldReturnCorrectNumber() throws ServiceException {
            doReturn(ENTITY_CARD).when(mockRepository).findDiscountCardByNumber(ENTITY_CARD.getNumber());
            doReturn(MODEL_CARD).when(mockModelMapper).toModel(ENTITY_CARD);

            DiscountCardModel actual = service.findByNumber(ENTITY_CARD.getNumber());

            Assertions.assertThat(actual).isEqualTo(MODEL_CARD);
        }
        @Test
        void findByNumberShouldReturnNull() throws ServiceException {
            Assertions.assertThat(service.findByNumber(0)).isNull();
        }
        @Test
        void findByNumberShouldThrowServiceException() {
            int number = -1;
            Assertions.assertThatThrownBy(() -> service.findByNumber(number))
                    .isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    class Save {
        @Test
        void saveShouldBeCorrect() throws ServiceException {
            doReturn(ENTITY_CARD).when(mockModelMapper).toEntity(MODEL_CARD);
            doReturn(ENTITY_CARD).when(mockRepository).save(ENTITY_CARD);
            doReturn(MODEL_CARD).when(mockModelMapper).toModel(ENTITY_CARD);

            DiscountCardModel actual = service.save(MODEL_CARD);

            Assertions.assertThat(actual).isEqualTo(MODEL_CARD);
        }
        @Test
        void saveShouldThrowServiceException() {
            Assertions.assertThatThrownBy(() -> service.save(null))
                    .isInstanceOf(ServiceException.class);
        }
    }
    @Nested
    class Delete {
        @Test
        void deleteShouldBeCorrect() throws ServiceException {
            doReturn(true).when(mockRepository)
                    .existsById(ENTITY_CARD.getId());
            doNothing()
                    .when(mockRepository)
                    .deleteById(ENTITY_CARD.getId());

            service.delete(ENTITY_CARD.getId());

            verify(mockRepository)
                    .deleteById(ENTITY_CARD.getId());
        }
        @Test
        void deleteShouldThrowServiceException() {
            doReturn(false).when(mockRepository)
                    .existsById(ENTITY_CARD.getId());
            Assertions.assertThatThrownBy(() -> service.delete(ENTITY_CARD.getId())).isInstanceOf(ServiceException.class);
        }
    }
}