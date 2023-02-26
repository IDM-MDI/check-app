package ru.clevertec.test.checkapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.test.checkapp.cache.DeleteCache;
import ru.clevertec.test.checkapp.cache.GetCache;
import ru.clevertec.test.checkapp.cache.SaveCache;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.ProductModel;
import ru.clevertec.test.checkapp.repository.ProductRepository;
import ru.clevertec.test.checkapp.service.ProductService;
import ru.clevertec.test.checkapp.util.impl.ProductModelMapper;
import ru.clevertec.test.checkapp.validator.ListValidator;

import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.test.checkapp.exception.ExceptionCode.ENTITY_ALREADY_EXIST;
import static ru.clevertec.test.checkapp.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static ru.clevertec.test.checkapp.exception.ExceptionCode.ENTITY_NOT_VALID;
import static ru.clevertec.test.checkapp.exception.ExceptionCode.ID_LIST_IS_EMPTY;
import static ru.clevertec.test.checkapp.validator.ProductValidator.isProductValid;


@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, ProductModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    @GetCache(key = "#id", returnType = ProductModel.class)
    public ProductModel findByID(long id) throws ServiceException {
        return modelMapper.toModel(repository.findById(id)
                .orElseThrow(() -> new ServiceException(ENTITY_NOT_FOUND.toString())));
    }
    @Override
    @SaveCache(fieldName = "id",returnType = ProductModel.class)
    public ProductModel save(ProductModel model) throws ServiceException {
        beforeSaveValidating(model);
        Product savedProduct = repository.save(modelMapper.toEntity(model));
        return modelMapper.toModel(savedProduct);
    }

    @Override
    @DeleteCache(key = "#id", returnType = ProductModel.class)
    public void delete(long id) throws ServiceException {
        if(!repository.existsById(id)) {
            throw new ServiceException(ENTITY_NOT_FOUND.toString());
        }
        repository.deleteById(id);
    }

    @Override
    public List<ProductModel> findByID(List<Long> ids) throws ServiceException {
        if(ListValidator.isListEmpty(ids)) {
            throw new ServiceException(ID_LIST_IS_EMPTY.toString());
        }
        return findProducts(ids);
    }

    @Override

    public ProductModel update(long id, ProductModel model) throws ServiceException {
        model.setId(id);
        return modelMapper.toModel(repository.save(modelMapper.toEntity(model)));
    }

    private List<ProductModel> findProducts(List<Long> ids) throws ServiceException {
        List<ProductModel> products = new ArrayList<>();
        for (Long id : ids) {
            products.add(findByID(id));
        }
        return products;
    }

    private void beforeSaveValidating(ProductModel model) throws ServiceException {
        if(!isProductValid(model)) {
            throw new ServiceException(ENTITY_NOT_VALID.toString());
        } else if(repository.existsProductByName(model.getName())) {
            throw new ServiceException(ENTITY_ALREADY_EXIST.toString());
        }
    }
}
