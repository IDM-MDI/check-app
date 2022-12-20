package ru.clevertec.test.checkapp.model;

import lombok.*;

import java.math.BigDecimal;
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
    private Set<ProductModel> products;
    private DiscountCardModel discountCard;
    private BigDecimal totalPrice;
    private LocalDateTime createdTime = LocalDateTime.now();
}
