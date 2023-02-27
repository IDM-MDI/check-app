package ru.clevertec.test.checkapp.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.ProductModel;
import ru.clevertec.test.checkapp.repository.ProductRepository;
import ru.clevertec.test.checkapp.util.impl.ProductModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    private static final ProductModelMapper modelMapper = new ProductModelMapper();
    private static final Product ENTITY_PRODUCT = Product.builder()
            .id(1L)
            .name("test")
            .price(BigDecimal.valueOf(1))
            .isOnOffer(true)
            .build();
    private static final ProductModel MODEL_PRODUCT = modelMapper.toModel(ENTITY_PRODUCT);
    @Mock
    private ProductRepository mockRepository;
    @Mock
    private ProductModelMapper mockModelMapper;
    @InjectMocks
    private ProductServiceImpl service;
    @Test
    void findByIDShouldBeCorrect() throws ServiceException {
        long id = ENTITY_PRODUCT.getId();
        when(mockRepository.findById(id))
                .thenReturn(Optional.of(ENTITY_PRODUCT));
        when(mockModelMapper.toModel(ENTITY_PRODUCT))
                .thenReturn(MODEL_PRODUCT);
        ProductModel actual = service.findByID(id);
        Assertions.assertEquals(MODEL_PRODUCT,actual);
    }
    @Test
    void findByIDShouldThrowException() {
        long id = ENTITY_PRODUCT.getId();
        Assertions.assertThrows(ServiceException.class,() -> service.findByID(id));
    }
    @Test
    void saveShouldBeCorrect() throws ServiceException {
        when(mockModelMapper.toEntity(MODEL_PRODUCT))
                .thenReturn(ENTITY_PRODUCT);
        when(mockRepository.save(ENTITY_PRODUCT))
                .thenReturn(ENTITY_PRODUCT);
        when(mockModelMapper.toModel(ENTITY_PRODUCT))
                .thenReturn(MODEL_PRODUCT);
        ProductModel actual = service.save(MODEL_PRODUCT);
        Assertions.assertEquals(MODEL_PRODUCT,actual);
    }
    @Test
    void saveShouldThrowException() {
        Assertions.assertThrows(ServiceException.class,() -> service.save(null));
    }

    @Test
    void deleteShouldBeCorrect() throws ServiceException {
        long id = ENTITY_PRODUCT.getId();
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
        long id = ENTITY_PRODUCT.getId();
        when(mockRepository.existsById(id))
                .thenReturn(false);
        Assertions.assertThrows(ServiceException.class,() -> service.delete(id));
    }

    @Test
    void findByIDListShouldBeCorrect() throws ServiceException {
        long id = ENTITY_PRODUCT.getId();
        List<Long> longs = List.of(id);
        List<ProductModel> expected = List.of(MODEL_PRODUCT);

        when(mockRepository.findById(id))
                .thenReturn(Optional.of(ENTITY_PRODUCT));
        when(mockModelMapper.toModel(ENTITY_PRODUCT))
                .thenReturn(MODEL_PRODUCT);

        List<ProductModel> actual = service.findByID(longs);
        Assertions.assertEquals(expected,actual);
    }

}