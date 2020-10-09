package com.kui.products.forex;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public interface ForexService {
    Map<Currency, BigDecimal> getForexRates(Currency from, Currency... to) throws ForexRetrievalException;

    BigDecimal getConvertedPrice(BigDecimal price, Currency currency, Currency requestedCurrency) throws ForexRetrievalException;
}
