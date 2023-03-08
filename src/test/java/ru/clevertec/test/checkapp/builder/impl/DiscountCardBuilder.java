package ru.clevertec.test.checkapp.builder.impl;

import ru.clevertec.test.checkapp.builder.TestEntityBuilder;
import ru.clevertec.test.checkapp.builder.TestModelBuilder;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.model.DiscountCardModel;

import java.util.Objects;


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
        long id = (this.id == null) ? 0 : this.id;
        return DiscountCardModel.builder()
                .id(id)
                .number(number)
                .discount(discount)
                .build();
    }

    public DiscountCardBuilder withId(Long id) {
        return Objects.equals(this.id, id) ? this : new DiscountCardBuilder(id, this.number, this.discount);
    }

    public DiscountCardBuilder withNumber(int number) {
        return this.number == number ? this : new DiscountCardBuilder(this.id, number, this.discount);
    }

    public DiscountCardBuilder withDiscount(int discount) {
        return this.discount == discount ? this : new DiscountCardBuilder(this.id, this.number, discount);
    }
}