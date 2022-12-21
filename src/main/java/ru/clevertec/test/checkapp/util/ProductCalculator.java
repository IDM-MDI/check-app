package ru.clevertec.test.checkapp.util;

import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.model.CheckProduct;
import ru.clevertec.test.checkapp.model.ProductModel;

public class ProductCalculator {
    private static final int Offer_COUNT = 5;
    private static final int Offer_DISCOUNT = 10;
    private ProductCalculator() {}

    public static double calculateCertainProduct(ProductModel product, int count) {
        double price = product.getPrice() * count;
        return (product.isOnOffer() && count >= Offer_COUNT) ?
                price - calculateDiscount(price, Offer_DISCOUNT) : price;
    }
    public static double calculateTotalPriceWithoutDiscount(CheckModel check) {
        return check.getElements()
                .stream()
                .mapToDouble(CheckProduct::getTotalPrice)
                .sum();
    }
    public static double calculateTotalPriceWithDiscount(CheckModel check) {
        if(check.getDiscountCard() == null) {
            return check.getTotalPriceWithoutCard();
        }
        double totalPrice = check.getTotalPriceWithoutCard();
        return totalPrice - calculateDiscount(totalPrice, check.getDiscountCard().getDiscount());
    }
    public static double calculateDiscount(double price, int discount) {
        return (price / 100) * discount;
    }
}
