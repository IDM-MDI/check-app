package ru.clevertec.test.checkapp.service;

import org.springframework.stereotype.Service;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.ProductModel;

@Service
public interface ProductService {
    ProductModel findByID(long id) throws ServiceException;
    ProductModel save(ProductModel model) throws ServiceException;
    void delete(long id) throws ServiceException;
}
