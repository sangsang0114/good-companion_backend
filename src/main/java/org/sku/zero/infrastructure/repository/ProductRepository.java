package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Product;
import org.sku.zero.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findShopProductsByShop(Shop shop);

    @Modifying
    @Query(value = "INSERT INTO product (shop_id, name, price) " +
            "VALUES (:#{#product.shopId}, :#{#product.name}, :#{#product.price}) " +
            "ON DUPLICATE KEY UPDATE price = CASE WHEN price < :#{#product.price} THEN :#{#product.price} ELSE price END"
            , nativeQuery = true)
    void saveOrUpdateProduct(@Param("product") Product product);
}
