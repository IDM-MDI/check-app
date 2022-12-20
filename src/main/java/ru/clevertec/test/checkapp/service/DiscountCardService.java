package ru.clevertec.test.checkapp.service;

import org.springframework.stereotype.Service;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.DiscountCardModel;

@Service
public interface DiscountCardService {
    DiscountCardModel findByID(long id) throws ServiceException;
    DiscountCardModel save(DiscountCardModel model) throws ServiceException;
    void delete(long id) throws ServiceException;
}
