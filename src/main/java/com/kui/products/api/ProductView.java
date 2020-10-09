package com.kui.products.api;

import com.kui.products.forex.ForexRetrievalException;
import com.kui.products.forex.ForexService;
import com.kui.products.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

/**
 * A view of a product that the API may return.
 */
public class ProductView {
    public static final Logger logger = LoggerFactory.getLogger(ProductView.class);

    private UUID id;
    private String name;
    private String description;

    /**
     * This should be provided in requested currency.
     */
    private BigDecimal price;

     /**
     Base price and base currency provided by api
     useful for a UI to know if price is subject to forex changes.
      */
    private BigDecimal basePrice;
    /**
     @see #basePrice
     */
    private Currency baseCurrency;

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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    /**
     * Factory method for creating a view for a product.
     * @param product the product to create
     * @param requestedCurrency currency to convert price to
     * @param forexService the service for converting
     * @return the created product view
     */
    public static ProductView fromProduct(Product product, @Nullable Currency requestedCurrency, ForexService forexService) {
        if (requestedCurrency == null) {
            requestedCurrency = product.getCurrency();
        }

        var view = new ProductView();
        BeanUtils.copyProperties(product, view);

        view.baseCurrency = product.getCurrency();
        view.basePrice = product.getPrice();

        if (!product.getCurrency().equals(requestedCurrency)) {
            try {
                view.price = forexService.getConvertedPrice(product.getPrice(), product.getCurrency(), requestedCurrency);
            }
            catch (ForexRetrievalException e) {
                logger.error("Could not retrieve foreign exchange from currency {} to currency {}",
                    product.getCurrency().getCurrencyCode(),
                    requestedCurrency.getCurrencyCode(), e);

                return null;
            }
        }

        return view;
    }

    /**
     * Factory method for converting multiple products
     * @see #fromProduct(Product, Currency, ForexService)
     */
    public static ProductView[] fromProducts(Product[] products, @Nullable Currency requestedCurrency, ForexService forexService) {
        return Arrays.stream(products)
            .map(p -> fromProduct(p, requestedCurrency, forexService))
            .filter(Objects::nonNull)
            .toArray(ProductView[]::new);
    }
}
