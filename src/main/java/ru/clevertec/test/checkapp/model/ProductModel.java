package ru.clevertec.test.checkapp.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductModel {
    private long id;
    private String name;
    private int count;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
