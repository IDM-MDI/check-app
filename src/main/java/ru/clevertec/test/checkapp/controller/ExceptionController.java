package ru.clevertec.test.checkapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.test.checkapp.exception.ServiceException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<String> serviceException(ServiceException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
