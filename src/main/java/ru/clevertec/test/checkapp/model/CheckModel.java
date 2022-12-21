package ru.clevertec.test.checkapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CheckModel {
    private Set<CheckProduct> elements;
    private DiscountCardModel discountCard;
    private double totalPriceWithoutCard;
    private double totalPrice;
    private LocalDateTime createdTime;
}
