package com.kui.products.product;

import com.kui.products.forex.ForexService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Simplistic service for listing products. Currently contains 3 static products.
 */
@Service
public class ProductService {

    private final Product[] products;

    public ProductService() {
        // Everything is EUR in here. Fixer only allows some base currencies for free.
        // In the interest of making this work we'll use EUR).
        var currency = Currency.getInstance("EUR");
        this.products = new Product[]{
            Product.create("Headphones", "Special headphones for all you ear needs", currency, new BigDecimal("42.99")),
            Product.create("Glasses", "Lets you see for days", currency, new BigDecimal("30.00")),
            Product.create("5e DMG", "Want to GM?", currency, new BigDecimal("26.66"))
        };
    }

    public Product[] listProducts() {
        return products;
    }
}
