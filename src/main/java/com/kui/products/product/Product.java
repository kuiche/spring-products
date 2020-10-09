package com.kui.products.product;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

/**
 * Product data class. Persist as @Entity if a database is hooked up.
 */
public class Product {
    private UUID id;
    private String name;
    private String description;
    private Currency currency;
    private BigDecimal price;

    public Product() {}

    public static Product create(String name, String description, Currency currency, BigDecimal price) {
        var product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCurrency(currency);
        product.setId(UUID.randomUUID());
        return product;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
