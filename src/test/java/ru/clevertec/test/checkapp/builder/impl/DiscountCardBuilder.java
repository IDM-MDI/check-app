package ru.clevertec.test.checkapp.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.test.checkapp.builder.TestEntityBuilder;
import ru.clevertec.test.checkapp.builder.TestModelBuilder;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.model.DiscountCardModel;


@AllArgsConstructor
@NoArgsConstructor(staticName = "aDiscountCard")
@With
public class DiscountCardBuilder implements TestEntityBuilder<DiscountCard>, TestModelBuilder<DiscountCardModel> {
    private Long id = null;
    private int number = 1;
    private int discount = 1;
    @Override
    public DiscountCard buildToEntity() {
        return DiscountCard.builder()
                .id(id)
                .number(number)
                .discount(discount)
                .build();
    }

    @Override
    public DiscountCardModel buildToModel() {
        return DiscountCardModel.builder()
                .id(id)
                .number(number)
                .discount(discount)
                .build();
    }
}
