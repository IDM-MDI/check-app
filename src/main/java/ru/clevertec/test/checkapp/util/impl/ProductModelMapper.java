package ru.clevertec.test.checkapp.util.impl;

import org.springframework.stereotype.Component;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.model.ProductModel;
import ru.clevertec.test.checkapp.util.ModelMapper;

import java.util.Collections;
import java.util.List;

@Component
public class ProductModelMapper implements ModelMapper<Product, ProductModel> {

    @Override
    public Product toEntity(ProductModel model) {
        return model == null ? null :
                Product.builder()
                        .id(model.getId())
                        .name(model.getName())
                        .price(model.getPrice())
                        .isOnStack(model.isOnStack())
                        .build();
    }

    @Override
    public ProductModel toModel(Product entity) {
        return entity == null ? null :
                ProductModel.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .price(entity.getPrice())
                        .isOnStack(entity.isOnStack())
                        .build();
    }

    @Override
    public List<Product> toEntityList(List<ProductModel> modelList) {
        if(modelList == null || modelList.size() < 1) {
            return Collections.emptyList();
        }
        return modelList.stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<ProductModel> toModelList(List<Product> entityList) {
        if(entityList == null || entityList.size() < 1) {
            return Collections.emptyList();
        }
        return entityList.stream()
                .map(this::toModel)
                .toList();
    }
}
