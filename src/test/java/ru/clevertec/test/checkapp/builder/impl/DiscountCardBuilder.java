package ru.clevertec.test.checkapp.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.test.checkapp.builder.TestBuilder;
import ru.clevertec.test.checkapp.entity.DiscountCard;


@AllArgsConstructor
@NoArgsConstructor(staticName = "aDiscount")
@With
public class DiscountCardBuilder implements TestBuilder<DiscountCard> {
    private Long id = null;
    private int number = 1;
    private int discount = 1;
    @Override
    public DiscountCard build() {
        return DiscountCard.builder()
                .id(id)
                .number(number)
                .discount(discount)
                .build();
    }
}
