package ru.clevertec.test.checkapp.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
    
    @Nested
    class FindByID {
        @Test
        void findByIDShouldReturnCorrectValue() throws ServiceException {
            long id = ENTITY_PRODUCT.getId();
            doReturn(Optional.of(ENTITY_PRODUCT)).when(mockRepository).findById(id);
            doReturn(MODEL_PRODUCT).when(mockModelMapper).toModel(ENTITY_PRODUCT);
            ProductModel actual = service.findByID(id);
            Assertions.assertThat(actual).isEqualTo(MODEL_PRODUCT);
        }
        @Test
        void findByIDShouldThrowServiceException() {
            long id = ENTITY_PRODUCT.getId();
            Assertions.assertThatThrownBy(() -> service.findByID(id))
                    .isInstanceOf(ServiceException.class);
        }

        @Test
        void findByIDListShouldReturnCorrectValue() throws ServiceException {
            long id = ENTITY_PRODUCT.getId();
            List<Long> longs = List.of(id);
            List<ProductModel> expected = List.of(MODEL_PRODUCT);

            doReturn(Optional.of(ENTITY_PRODUCT)).when(mockRepository).findById(id);
            doReturn(MODEL_PRODUCT).when(mockModelMapper).toModel(ENTITY_PRODUCT);

            List<ProductModel> actual = service.findByID(longs);
            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }
    @Nested
    class Save {
        @Test
        void saveShouldReturnCorrectValue() throws ServiceException {
            doReturn(ENTITY_PRODUCT).when(mockModelMapper).toEntity(MODEL_PRODUCT);
            doReturn(ENTITY_PRODUCT).when(mockRepository).save(ENTITY_PRODUCT);
            doReturn(MODEL_PRODUCT).when(mockModelMapper).toModel(ENTITY_PRODUCT);
            ProductModel actual = service.save(MODEL_PRODUCT);
            Assertions.assertThat(actual).isEqualTo(MODEL_PRODUCT);
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
        void deleteShouldReturnCorrectValue() throws ServiceException {
            long id = ENTITY_PRODUCT.getId();
            doReturn(true).when(mockRepository).existsById(id);
            doNothing()
                    .when(mockRepository)
                    .deleteById(id);
            service.delete(id);
            verify(mockRepository)
                    .deleteById(id);
        }
        @Test
        void deleteShouldThrowServiceException() {
            long id = ENTITY_PRODUCT.getId();
            doReturn(false).when(mockRepository).existsById(id);
            Assertions.assertThatThrownBy(() -> service.delete(id))
                    .isInstanceOf(ServiceException.class);
        }
    }
}