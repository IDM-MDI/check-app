package ru.clevertec.test.checkapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.test.checkapp.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsProductByName(String name);
}
