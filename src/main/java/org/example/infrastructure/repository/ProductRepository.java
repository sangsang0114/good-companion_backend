package org.example.infrastructure.repository;

import org.example.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(value = "INSERT INTO product (shop_id, name, price) " +
            "VALUES (:#{#product.shopId}, :#{#product.name}, :#{#product.price}) " +
            "ON DUPLICATE KEY UPDATE price = CASE WHEN price < :#{#product.price} THEN :#{#product.price} ELSE price END"
            , nativeQuery = true)
    void saveOrUpdateProduct(@Param("product") Product product);
}
