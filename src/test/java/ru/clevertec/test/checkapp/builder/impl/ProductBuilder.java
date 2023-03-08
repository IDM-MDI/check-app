package ru.clevertec.test.checkapp.builder.impl;

import ru.clevertec.test.checkapp.builder.TestEntityBuilder;
import ru.clevertec.test.checkapp.builder.TestModelBuilder;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.math.BigDecimal;
import java.util.Objects;

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
        long id = (this.id == null) ? 0 : this.id;
        return ProductModel.builder()
                .id(id)
                .name(name)
                .price(price.doubleValue())
                .offer(isOnOffer)
                .build();
    }

    public ProductBuilder withId(Long id) {
        return Objects.equals(this.id, id) ? this : new ProductBuilder(id, this.name, this.price, this.isOnOffer);
    }

    public ProductBuilder withName(String name) {
        return Objects.equals(this.name, name) ? this : new ProductBuilder(this.id, name, this.price, this.isOnOffer);
    }

    public ProductBuilder withPrice(BigDecimal price) {
        return Objects.equals(this.price, price) ? this : new ProductBuilder(this.id, this.name, price, this.isOnOffer);
    }

    public ProductBuilder withOnOffer(boolean isOnOffer) {
        return this.isOnOffer == isOnOffer ? this : new ProductBuilder(this.id, this.name, this.price, isOnOffer);
    }
}
