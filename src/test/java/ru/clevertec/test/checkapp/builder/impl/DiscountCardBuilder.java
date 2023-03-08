package ru.clevertec.test.checkapp.builder.impl;

import lombok.With;
import ru.clevertec.test.checkapp.builder.TestEntityBuilder;
import ru.clevertec.test.checkapp.builder.TestModelBuilder;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.model.DiscountCardModel;


@With
public class DiscountCardBuilder implements TestEntityBuilder<DiscountCard>, TestModelBuilder<DiscountCardModel> {
    private Long id = null;
    private int number = 1;
    private int discount = 1;

    public DiscountCardBuilder(Long id, int number, int discount) {
        this.id = id;
        this.number = number;
        this.discount = discount;
    }

    private DiscountCardBuilder() {
    }

    public static DiscountCardBuilder aDiscountCard() {
        return new DiscountCardBuilder();
    }

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
