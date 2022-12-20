package ru.clevertec.test.checkapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.test.checkapp.model.CheckModel;

@RestController
@RequestMapping("/api/v1/check")
public class CheckController {

    @GetMapping
    public CheckModel buyProduct() {
        return null;
    }
}
