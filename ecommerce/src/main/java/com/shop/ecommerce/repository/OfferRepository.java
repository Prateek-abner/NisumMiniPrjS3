package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

    @Query("SELECT o FROM Offer o WHERE o.product.productId = :productId")
    List<Offer> findByProductId(@Param("productId") String productId);
}
