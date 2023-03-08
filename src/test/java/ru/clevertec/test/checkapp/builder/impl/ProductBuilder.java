package ru.clevertec.test.checkapp.builder.impl;

import lombok.With;
import ru.clevertec.test.checkapp.builder.TestEntityBuilder;
import ru.clevertec.test.checkapp.builder.TestModelBuilder;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.math.BigDecimal;

@With
public class ProductBuilder implements TestEntityBuilder<Product>, TestModelBuilder<ProductModel> {
    private Long id = null;
    private String name = "testName";
    private BigDecimal price = new BigDecimal(1);
    private boolean isOnOffer = true;

    public ProductBuilder(Long id, String name, BigDecimal price, boolean isOnOffer) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isOnOffer = isOnOffer;
    }

    private ProductBuilder() {
    }

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    @Override
    public Product buildToEntity() {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .isOnOffer(isOnOffer)
                .build();
    }

    @Override
    public ProductModel buildToModel() {
        return ProductModel.builder()
                .id(id)
                .name(name)
                .price(price.doubleValue())
                .offer(isOnOffer)
                .build();
    }
}
