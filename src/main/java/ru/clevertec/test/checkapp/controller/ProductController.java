package ru.clevertec.test.checkapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.test.checkapp.aop.GetCache;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.model.ProductModel;
import ru.clevertec.test.checkapp.service.ProductService;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }
    @GetMapping("/{id}")
    public ProductModel findByID(@PathVariable long id) throws ServiceException {
        return service.findByID(id);
    }

    @PostMapping
    public ProductModel save(@RequestBody ProductModel model) throws ServiceException {
        return service.save(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) throws ServiceException {
        service.delete(id);
        return ResponseEntity.ok("Product was successfully deleted");
    }
}
