package com.kui.products.api;

import com.kui.products.forex.ForexRetrievalException;
import com.kui.products.forex.ForexService;
import com.kui.products.forex.fixer.FixerService;
import com.kui.products.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class ProductViewTest {

    @Test
    void fromProduct() throws ForexRetrievalException {
        Product p = Product.create("Test Product", "Test Desc", Currency.getInstance("EUR"), new BigDecimal("9.99"));
        var gbp = Currency.getInstance("GBP");

        ForexService forex = Mockito.mock(ForexService.class);
        Mockito.when(forex.getConvertedPrice(Mockito.eq(p.getPrice()), Mockito.eq(p.getCurrency()), Mockito.eq(gbp)))
            .thenReturn(new BigDecimal("1.44"));

        var view = ProductView.fromProduct(p, gbp, forex);

        Assertions.assertEquals(p.getId(), view.getId());
        Assertions.assertEquals(p.getName(), view.getName());
        Assertions.assertEquals(p.getCurrency(), view.getBaseCurrency());
        Assertions.assertEquals(p.getPrice(), view.getBasePrice());
        Assertions.assertEquals(new BigDecimal("1.44"), view.getPrice());

    }
}
