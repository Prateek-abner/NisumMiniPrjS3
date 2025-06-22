package com.shop.ecommerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Integer offerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "offer_description", length = 200)
    private String offerDescription;

    // Constructors
    public Offer() {}

    public Offer(Product product, String offerDescription) {
        this.product = product;
        this.offerDescription = offerDescription;
    }

    // Getters and Setters
    public Integer getOfferId() { return offerId; }
    public void setOfferId(Integer offerId) { this.offerId = offerId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getOfferDescription() { return offerDescription; }
    public void setOfferDescription(String offerDescription) { this.offerDescription = offerDescription; }
}
