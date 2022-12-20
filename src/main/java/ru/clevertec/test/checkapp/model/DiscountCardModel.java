package ru.clevertec.test.checkapp.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DiscountCardModel {
    private long id;
    private int number;
    private int discount;
}
