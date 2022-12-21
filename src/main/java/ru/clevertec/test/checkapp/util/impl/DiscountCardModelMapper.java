package ru.clevertec.test.checkapp.util.impl;

import org.springframework.stereotype.Component;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.util.ModelMapper;

import java.util.Collections;
import java.util.List;

@Component
public class DiscountCardModelMapper implements ModelMapper<DiscountCard, DiscountCardModel> {
    @Override
    public DiscountCard toEntity(DiscountCardModel model) {
        return model == null ? null :
                DiscountCard.builder()
                        .id(model.getId())
                        .number(model.getNumber())
                        .discount(model.getDiscount())
                        .build();
    }

    @Override
    public DiscountCardModel toModel(DiscountCard entity) {
        return entity == null ? null :
                DiscountCardModel.builder()
                        .id(entity.getId())
                        .number(entity.getNumber())
                        .discount(entity.getDiscount())
                        .build();
    }

    @Override
    public List<DiscountCard> toEntityList(List<DiscountCardModel> modelList) {
        if(modelList == null || modelList.isEmpty()) {
            return Collections.emptyList();
        }
        return modelList.stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<DiscountCardModel> toModelList(List<DiscountCard> entityList) {
        if(entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        return entityList.stream()
                .map(this::toModel)
                .toList();
    }
}
