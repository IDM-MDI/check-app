package ru.clevertec.test.checkapp.service.impl;

import org.junit.jupiter.api.Assertions;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountCardServiceImplTest {
    private static final DiscountCardModelMapper modelMapper = new DiscountCardModelMapper();;
    private static final DiscountCard ENTITY_CARD = DiscountCard.builder()
            .id(1L)
            .number(1)
            .discount(1)
            .build();
    private static final DiscountCardModel MODEL_CARD = modelMapper.toModel(ENTITY_CARD);
    @Mock
    private DiscountCardRepository mockRepository;
    @Mock
    private DiscountCardModelMapper mockModelMapper;
    @InjectMocks
    private DiscountCardServiceImpl service;


    @Test
    void findByIDShouldBeCorrect() throws ServiceException {
        long id = ENTITY_CARD.getId();
        when(mockRepository.findById(id))
                .thenReturn(Optional.of(ENTITY_CARD));
        when(mockModelMapper.toModel(ENTITY_CARD))
                .thenReturn(MODEL_CARD);
        DiscountCardModel actual = service.findByID(id);
        Assertions.assertEquals(MODEL_CARD,actual);
    }
    @Test
    void findByIDShouldThrowException() {
        long id = ENTITY_CARD.getId();
        Assertions.assertThrows(ServiceException.class,() -> service.findByID(id));
    }

    @Test
    void findByNumberShouldBeCorrect() throws ServiceException {
        int number = ENTITY_CARD.getNumber();
        when(mockRepository.findDiscountCardByNumber(number))
                .thenReturn(ENTITY_CARD);
        when(mockModelMapper.toModel(ENTITY_CARD))
                .thenReturn(MODEL_CARD);
        DiscountCardModel actual = service.findByNumber(number);
        Assertions.assertEquals(MODEL_CARD,actual);
    }
    @Test
    void findByNumberShouldBeNull() throws ServiceException {
        Assertions.assertNull(service.findByNumber(0));
    }
    @Test
    void findByNumberShouldThrowException() {
        int number = -1;
        Assertions.assertThrows(ServiceException.class,() -> service.findByNumber(number));
    }
    @Test
    void saveShouldBeCorrect() throws ServiceException {
        when(mockModelMapper.toEntity(MODEL_CARD))
                .thenReturn(ENTITY_CARD);
        when(mockRepository.save(ENTITY_CARD))
                .thenReturn(ENTITY_CARD);
        when(mockModelMapper.toModel(ENTITY_CARD))
                .thenReturn(MODEL_CARD);
        DiscountCardModel actual = service.save(MODEL_CARD);
        Assertions.assertEquals(MODEL_CARD,actual);
    }
    @Test
    void saveShouldThrowException() {
        Assertions.assertThrows(ServiceException.class,() -> service.save(null));
    }
    @Test
    void deleteShouldBeCorrect() throws ServiceException {
        long id = ENTITY_CARD.getId();
        when(mockRepository.existsById(id))
                .thenReturn(true);
        doNothing()
                .when(mockRepository)
                .deleteById(id);
        service.delete(id);
        verify(mockRepository)
                .deleteById(id);
    }
    @Test
    void deleteShouldThrowException() {
        long id = ENTITY_CARD.getId();
        when(mockRepository.existsById(id))
                .thenReturn(false);
        Assertions.assertThrows(ServiceException.class,() -> service.delete(id));
    }
}