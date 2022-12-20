package ru.clevertec.test.checkapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.ProductModel;
import ru.clevertec.test.checkapp.repository.ProductRepository;
import ru.clevertec.test.checkapp.service.ProductService;
import ru.clevertec.test.checkapp.util.impl.ProductModelMapper;

import static ru.clevertec.test.checkapp.exception.ExceptionCode.ENTITY_ALREADY_EXIST;
import static ru.clevertec.test.checkapp.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static ru.clevertec.test.checkapp.exception.ExceptionCode.ENTITY_NOT_VALID;
import static ru.clevertec.test.checkapp.validator.ProductValidator.isProductValid;


public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, ProductModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductModel findByID(long id) throws ServiceException {
        return modelMapper.toModel(repository.findById(id)
                .orElseThrow(() -> new ServiceException(ENTITY_NOT_FOUND.toString())));
    }
    @Override
    public ProductModel save(ProductModel model) throws ServiceException {
        beforeSaveValidating(model);
        Product savedProduct = repository.save(modelMapper.toEntity(model));
        return modelMapper.toModel(savedProduct);
    }

    @Override
    public void delete(long id) throws ServiceException {
        if(!repository.existsById(id)) {
            throw new ServiceException(ENTITY_NOT_FOUND.toString());
        }
        repository.deleteById(id);
    }
    private void beforeSaveValidating(ProductModel model) throws ServiceException {
        if(!isProductValid(model)) {
            throw new ServiceException(ENTITY_NOT_VALID.toString());
        } else if(repository.existsProductByName(model.getName())) {
            throw new ServiceException(ENTITY_ALREADY_EXIST.toString());
        }
    }
}
