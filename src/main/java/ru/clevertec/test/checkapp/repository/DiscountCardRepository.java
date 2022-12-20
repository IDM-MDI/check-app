package ru.clevertec.test.checkapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.test.checkapp.entity.DiscountCard;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard,Long> {
    boolean existsDiscountCardByNumber(int number);
    DiscountCard findDiscountCardByNumber(int number);
}
