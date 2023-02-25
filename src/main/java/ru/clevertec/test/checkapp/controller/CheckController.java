package ru.clevertec.test.checkapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.service.CheckService;
import ru.clevertec.test.checkapp.util.JsonSerializer;

@RestController
@RequestMapping("/api/v1/check")
@RequiredArgsConstructor
public class CheckController {
    private final CheckService service;
    @GetMapping
    public CheckModel buyProduct(HttpServletRequest request) throws ServiceException {
        return service.getCheck(request.getQueryString());
    }
}
