package com.shop.ecommerce.service;

import com.shop.ecommerce.dto.ProductDTO;
import com.shop.ecommerce.entity.Product;
import com.shop.ecommerce.entity.ProductSize;
import com.shop.ecommerce.entity.Offer;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.repository.ProductSizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductById(String id) {
        return productRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<ProductDTO> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String name) {
        return productRepository.findByProductNameContaining(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getDiscountedProducts() {
        return productRepository.findDiscountedProducts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAvailableProducts() {
        return productRepository.findAvailableProducts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getLatestProducts() {
        return productRepository.findLatestProducts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllBrands() {
        return productRepository.findAllBrands();
    }

    public List<String> getAllSizes() {
        return productSizeRepository.findAllSizes();
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null);
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setDiscountPercent(product.getDiscountPercent());
        dto.setQuantityInStock(product.getQuantityInStock());
        dto.setBrand(product.getBrand());
        dto.setImageUrl(product.getImageUrl());

        if (product.getSizes() != null) {
            dto.setSizes(product.getSizes().stream()
                    .map(ProductSize::getSize)
                    .collect(Collectors.toList()));
        }

        if (product.getOffers() != null) {
            dto.setOffers(product.getOffers().stream()
                    .map(Offer::getOfferDescription)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
