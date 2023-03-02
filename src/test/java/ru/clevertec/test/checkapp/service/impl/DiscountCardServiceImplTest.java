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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountCardServiceImplTest {
    static final DiscountCardModelMapper modelMapper = new DiscountCardModelMapper();;
    static final DiscountCard ENTITY_CARD = DiscountCard.builder()
            .id(1L)
            .number(1)
            .discount(1)
            .build();
    static final DiscountCardModel MODEL_CARD = modelMapper.toModel(ENTITY_CARD);
    @Mock
    DiscountCardRepository mockRepository;
    @Mock
    DiscountCardModelMapper mockModelMapper;
    @InjectMocks
    DiscountCardServiceImpl service;


    @Nested
    class FindByID {
        @Test
        void findByIDShouldReturnCorrectCard() throws ServiceException {
            long id = ENTITY_CARD.getId();
            doReturn(Optional.of(ENTITY_CARD)).when(mockRepository).findById(id);
            doReturn(MODEL_CARD).when(mockModelMapper).toModel(ENTITY_CARD);

            DiscountCardModel actual = service.findByID(id);
            Assertions.assertThat(actual).isEqualTo(MODEL_CARD);
        }
        @Test
        void findByIDShouldThrowException() {
            long id = ENTITY_CARD.getId();
            Assertions.assertThatThrownBy(() -> service.findByID(id))
                    .isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    class FindByNumber {
        @Test
        void findByNumberShouldReturnCorrectNumber() throws ServiceException {
            int number = ENTITY_CARD.getNumber();
            when(mockRepository.findDiscountCardByNumber(number))
                    .thenReturn(ENTITY_CARD);
            when(mockModelMapper.toModel(ENTITY_CARD))
                    .thenReturn(MODEL_CARD);
            DiscountCardModel actual = service.findByNumber(number);
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
            long id = ENTITY_CARD.getId();
            doReturn(true).when(mockRepository)
                    .existsById(id);
            doNothing()
                    .when(mockRepository)
                    .deleteById(id);
            service.delete(id);
            verify(mockRepository)
                    .deleteById(id);
        }
        @Test
        void deleteShouldThrowServiceException() {
            long id = ENTITY_CARD.getId();
            doReturn(false).when(mockRepository)
                    .existsById(id);
            Assertions.assertThatThrownBy(() -> service.delete(id)).isInstanceOf(ServiceException.class);
        }
    }
}