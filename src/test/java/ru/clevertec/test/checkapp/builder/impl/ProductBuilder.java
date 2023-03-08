package ru.clevertec.test.checkapp.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.test.checkapp.builder.TestEntityBuilder;
import ru.clevertec.test.checkapp.builder.TestModelBuilder;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aProduct")
@With
public class ProductBuilder implements TestEntityBuilder<Product>, TestModelBuilder<ProductModel> {
    private Long id = null;
    private String name = "testName";
    private BigDecimal price = new BigDecimal("1");
    private boolean isOnOffer = true;

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
