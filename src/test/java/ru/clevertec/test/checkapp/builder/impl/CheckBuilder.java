package ru.clevertec.test.checkapp.builder.impl;

import ru.clevertec.test.checkapp.builder.TestModelBuilder;
import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.model.CheckProduct;
import ru.clevertec.test.checkapp.model.DiscountCardModel;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static ru.clevertec.test.checkapp.builder.impl.DiscountCardBuilder.aDiscountCard;

public class CheckBuilder implements TestModelBuilder<CheckModel> {
    private Set<CheckProduct> elements = new HashSet<>();
    private DiscountCardModel discountCard = aDiscountCard().buildToModel();
    private double totalPriceWithoutCard = 1;
    private double totalPrice = 1;
    private LocalDateTime createdTime = null;

    public CheckBuilder(Set<CheckProduct> elements, DiscountCardModel discountCard, double totalPriceWithoutCard, double totalPrice, LocalDateTime createdTime) {
        this.elements = elements;
        this.discountCard = discountCard;
        this.totalPriceWithoutCard = totalPriceWithoutCard;
        this.totalPrice = totalPrice;
        this.createdTime = createdTime;
    }

    private CheckBuilder() {
    }

    public static CheckBuilder aCheck() {
        return new CheckBuilder();
    }

    @Override
    public CheckModel buildToModel() {
        return CheckModel.builder()
                .elements(elements)
                .discountCard(discountCard)
                .totalPrice(totalPrice)
                .totalPriceWithoutCard(totalPriceWithoutCard)
                .createdTime(createdTime)
                .build();
    }

    public CheckBuilder withElements(Set<CheckProduct> elements) {
        return this.elements == elements ? this : new CheckBuilder(elements, this.discountCard, this.totalPriceWithoutCard, this.totalPrice, this.createdTime);
    }

    public CheckBuilder withDiscountCard(DiscountCardModel discountCard) {
        return this.discountCard == discountCard ? this : new CheckBuilder(this.elements, discountCard, this.totalPriceWithoutCard, this.totalPrice, this.createdTime);
    }

    public CheckBuilder withTotalPriceWithoutCard(double totalPriceWithoutCard) {
        return this.totalPriceWithoutCard == totalPriceWithoutCard ? this : new CheckBuilder(this.elements, this.discountCard, totalPriceWithoutCard, this.totalPrice, this.createdTime);
    }

    public CheckBuilder withTotalPrice(double totalPrice) {
        return this.totalPrice == totalPrice ? this : new CheckBuilder(this.elements, this.discountCard, this.totalPriceWithoutCard, totalPrice, this.createdTime);
    }

    public CheckBuilder withCreatedTime(LocalDateTime createdTime) {
        return this.createdTime == createdTime ? this : new CheckBuilder(this.elements, this.discountCard, this.totalPriceWithoutCard, this.totalPrice, createdTime);
    }
}
