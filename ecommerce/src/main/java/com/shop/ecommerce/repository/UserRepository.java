package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM Users1 WHERE Email = :email LIMIT 1", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM Users1 WHERE Email = :email", nativeQuery = true)
    Integer existsByEmailNative(@Param("email") String email);

    @Query(value = "SELECT * FROM Users1 WHERE FirstName LIKE CONCAT('%', :name, '%') OR LastName LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<User> findByNameContaining(@Param("name") String name);

    @Query(value = "SELECT * FROM Users1 ORDER BY created_at DESC", nativeQuery = true)
    List<User> findAllOrderByCreatedAt();

    @Query(value = "SELECT * FROM Users1 WHERE PhoneNumber IS NOT NULL AND PhoneNumber != ''", nativeQuery = true)
    List<User> findUsersWithPhoneNumber();

    @Query(value = "SELECT * FROM Users1 WHERE created_at >= DATE_SUB(NOW(), INTERVAL :days DAY)", nativeQuery = true)
    List<User> findRecentUsers(@Param("days") int days);

    
    default boolean existsByEmail(String email) {
        Integer result = existsByEmailNative(email);
        return result != null && result > 0;
    }
}
