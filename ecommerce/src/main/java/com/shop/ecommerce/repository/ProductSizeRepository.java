package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Integer> {

    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.productId = :productId")
    List<ProductSize> findByProductId(@Param("productId") String productId);

    @Query("SELECT DISTINCT ps.size FROM ProductSize ps ORDER BY ps.size")
    List<String> findAllSizes();
}
