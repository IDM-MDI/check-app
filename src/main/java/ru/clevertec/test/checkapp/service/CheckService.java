package ru.clevertec.test.checkapp.service;

import org.springframework.stereotype.Service;
import ru.clevertec.test.checkapp.model.CheckModel;

@Service
public interface CheckService {
    CheckModel getCheck(String queryString);
}
