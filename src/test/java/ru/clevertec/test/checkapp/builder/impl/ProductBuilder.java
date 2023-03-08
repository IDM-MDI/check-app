package ru.clevertec.test.checkapp.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.test.checkapp.builder.TestBuilder;
import ru.clevertec.test.checkapp.entity.Product;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aProduct")
@With
public class ProductBuilder implements TestBuilder<Product> {
    private Long id = null;
    private String name = "testName";
    private BigDecimal price = new BigDecimal("1");
    private boolean isOnOffer = true;

    @Override
    public Product build() {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .isOnOffer(isOnOffer)
                .build();
    }
}
