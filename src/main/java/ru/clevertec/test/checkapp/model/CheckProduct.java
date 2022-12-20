package ru.clevertec.test.checkapp.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CheckProduct {
    private List<ProductModel> products;
    private int count;
    private BigDecimal totalPrice;
}
