package ru.clevertec.test.checkapp.builder.impl;

import ru.clevertec.test.checkapp.builder.TestModelBuilder;
import ru.clevertec.test.checkapp.model.CheckProduct;
import ru.clevertec.test.checkapp.model.ProductModel;

import static ru.clevertec.test.checkapp.builder.impl.ProductBuilder.aProduct;

public class CheckProductBuilder implements TestModelBuilder<CheckProduct> {
    private ProductModel product = aProduct().buildToModel();
    private int count = 1;
    private double totalPrice = 1;

    public CheckProductBuilder(ProductModel product, int count, double totalPrice) {
        this.product = product;
        this.count = count;
        this.totalPrice = totalPrice;
    }

    private CheckProductBuilder() {
    }

    public static CheckProductBuilder aCheckProduct() {
        return new CheckProductBuilder();
    }

    @Override
    public CheckProduct buildToModel() {
        return CheckProduct.builder()
                .product(product)
                .count(count)
                .totalPrice(totalPrice)
                .build();
    }

    public CheckProductBuilder withProduct(ProductModel product) {
        return this.product == product ? this : new CheckProductBuilder(product, this.count, this.totalPrice);
    }

    public CheckProductBuilder withCount(int count) {
        return this.count == count ? this : new CheckProductBuilder(this.product, count, this.totalPrice);
    }

    public CheckProductBuilder withTotalPrice(double totalPrice) {
        return this.totalPrice == totalPrice ? this : new CheckProductBuilder(this.product, this.count, totalPrice);
    }
}
