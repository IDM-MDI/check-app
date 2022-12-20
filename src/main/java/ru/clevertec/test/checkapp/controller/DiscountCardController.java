package ru.clevertec.test.checkapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.service.DiscountCardService;

@RestController
@RequestMapping("/api/v1/card")
public class DiscountCardController {
    private final DiscountCardService service;

    @Autowired
    public DiscountCardController(DiscountCardService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public DiscountCardModel findCardByID(@PathVariable long id) throws ServiceException {
        return service.findByID(id);
    }
    @PostMapping
    public DiscountCardModel save(@RequestBody DiscountCardModel model) throws ServiceException {
        return service.save(model);
    }
}
