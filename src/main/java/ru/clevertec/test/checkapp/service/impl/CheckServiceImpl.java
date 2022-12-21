package ru.clevertec.test.checkapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.CheckModel;
import ru.clevertec.test.checkapp.model.CheckProduct;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.model.ProductModel;
import ru.clevertec.test.checkapp.service.CheckService;
import ru.clevertec.test.checkapp.service.DiscountCardService;
import ru.clevertec.test.checkapp.service.ProductService;
import ru.clevertec.test.checkapp.util.ProductCalculator;
import ru.clevertec.test.checkapp.util.RequestParameterUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CheckServiceImpl implements CheckService {
    private final ProductService productService;
    private final DiscountCardService cardService;

    @Autowired
    public CheckServiceImpl(ProductService productService, DiscountCardService cardService) {
        this.productService = productService;
        this.cardService = cardService;
    }

    @Override
    public CheckModel getCheck(String queryString) throws ServiceException {
        List<Long> productIds = RequestParameterUtil.findProducts(queryString);
        int number = RequestParameterUtil.findDiscountCardNumber(queryString);
        return createCheck(productService.findByID(productIds),cardService.findByNumber(number));
    }

    private CheckModel createCheck(List<ProductModel> products, DiscountCardModel card) {
        CheckModel check = CheckModel.builder()
                .discountCard(card)
                .elements(createCheckProduct(products))
                .createdTime(LocalDateTime.now())
                .build();
        fillTotalPrice(check);
        return check;
    }
    private Set<CheckProduct> createCheckProduct(List<ProductModel> products) {
        Map<ProductModel, Integer> productCountMap = createProductCountMap(products);
        Set<CheckProduct> result = new HashSet<>();
        productCountMap.forEach((k,v) -> result.add(CheckProduct.builder()
                .product(k)
                .totalPrice(ProductCalculator.calculateCertainProduct(k, v))
                .count(v)
                .build()));
        return result;
    }
    private Map<ProductModel,Integer> createProductCountMap(List<ProductModel> products) {
        Map<ProductModel,Integer> productCountMap = new HashMap<>();
        products.forEach(product -> {
            if(productCountMap.get(product) != null) {
                productCountMap.put(product, productCountMap.get(product) + 1);
            }
            productCountMap.putIfAbsent(product, 1);
        });
        return productCountMap;
    }
    private void fillTotalPrice(CheckModel check) {
        check.setTotalPriceWithoutCard(ProductCalculator.calculateTotalPriceWithoutDiscount(check));
        check.setTotalPrice(ProductCalculator.calculateTotalPriceWithDiscount(check));
    }
}
