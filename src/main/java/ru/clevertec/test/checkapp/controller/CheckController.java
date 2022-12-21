package ru.clevertec.test.checkapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.service.CheckService;

@RestController
@RequestMapping("/api/v1/check")
public class CheckController {
    private final CheckService service;

    @Autowired
    public CheckController(CheckService service) {
        this.service = service;
    }

    @GetMapping
    public CheckModel buyProduct(HttpServletRequest request) throws ServiceException {
        return service.getCheck(request.getQueryString());
    }
}
