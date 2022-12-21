package ru.clevertec.test.checkapp.service;

import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.DiscountCardModel;

public interface DiscountCardService {
    DiscountCardModel findByID(long id) throws ServiceException;
    DiscountCardModel findByNumber(int number) throws ServiceException;
    DiscountCardModel save(DiscountCardModel model) throws ServiceException;
    void delete(long id) throws ServiceException;
}
