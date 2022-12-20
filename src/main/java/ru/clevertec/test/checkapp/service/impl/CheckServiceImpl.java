package ru.clevertec.test.checkapp.service.impl;

import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.service.CheckService;
import ru.clevertec.test.checkapp.util.RequestParameterUtil;

import java.util.List;

public class CheckServiceImpl implements CheckService {
    @Override
    public CheckModel getCheck(String queryString) {
        List<Long> productIds = RequestParameterUtil.findProducts(queryString);
        int number = RequestParameterUtil.findDiscountCardNumber(queryString);

        return null;
    }
}
