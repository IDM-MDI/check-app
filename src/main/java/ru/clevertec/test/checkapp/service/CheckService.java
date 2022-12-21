package ru.clevertec.test.checkapp.service;

import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.CheckModel;


public interface CheckService {
    CheckModel getCheck(String queryString) throws ServiceException;
}
