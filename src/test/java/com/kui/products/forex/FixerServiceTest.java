package com.kui.products.forex;

import com.kui.products.forex.fixer.FixerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

class FixerServiceTest {

    @Test
    void getForexRates() {
        var fixerSvc = new FixerService("d41a68a9dd4dc6beb68e5618e71548eb", "http://data.fixer.io/api");
        var gbp = Currency.getInstance("GBP");
        var cad = Currency.getInstance("CAD");
        Map<Currency, BigDecimal> rates = null;
        try {
            rates = fixerSvc.getForexRates(Currency.getInstance("EUR"), gbp, cad);
        } catch (ForexRetrievalException e) {
            Assertions.fail(e);
        }

        Assertions.assertEquals(2, rates.size());
        Assertions.assertNotNull(rates.get(gbp));
        Assertions.assertNotNull(rates.get(cad));
    }
}
