package com.kui.products.forex.fixer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.kui.products.forex.ForexRetrievalException;
import com.kui.products.forex.ForexService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Retrieves forex rates from fixer.
 */
@Service
public class FixerService implements ForexService {

    private String apiKey;
    private String apiUrl;

    private Cache<Currency, BigDecimal> cache = CacheBuilder
        .newBuilder()
        .expireAfterWrite(2, TimeUnit.HOURS)
        .build();

    public FixerService(
        @Value("${service.fixer.apikey:d41a68a9dd4dc6beb68e5618e71548eb}") String apiKey,
        @Value("${service.fixer.apiurl:http://data.fixer.io/api}") String apiUrl
        ) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    @Override
    public Map<Currency, BigDecimal> getForexRates(Currency from, Currency... to) throws ForexRetrievalException {

        var cached = cache.getAllPresent(Lists.newArrayList(to));
        var anyMissing = Arrays.stream(to).anyMatch(c -> cache.getIfPresent(c) == null);

        if (!anyMissing) {
            return cached;
        }

        return loadForexRates(from, to);

    }

    private Map<Currency, BigDecimal> loadForexRates(Currency from, Currency... to) throws ForexRetrievalException {
        var templateUrl = apiUrl + "/latest?access_key={access_key}&base={base}&symbols={symbols}";

        var currencyString = String.join(",", Arrays.stream(to).map(Currency::getCurrencyCode).collect(Collectors.toSet()));

        var params = new HashMap<String, String>();
        params.put("access_key", this.apiKey);
        params.put("base", from.getCurrencyCode());
        params.put("symbols", currencyString);

        var template = new RestTemplate();

        try {
            var entity = template.getForEntity(templateUrl, FixerResponse.class, params);

            if (entity.getStatusCode().is2xxSuccessful() && entity.getBody() != null && entity.getBody().isSuccess()) {
                entity.getBody().getRates().forEach((k, v) -> cache.put(k, v));
                return entity.getBody().getRates();
            }

            throw new ForexRetrievalException("Could not retrieve forex rates");
        }
        catch (Exception e) {
            // Network exceptions etc
            throw new ForexRetrievalException("An exception occured when requesting forex rates", e);
        }
    }

    @Override
    public BigDecimal getConvertedPrice(BigDecimal price, Currency currency, Currency requestedCurrency) throws ForexRetrievalException {

        var rate = getForexRates(currency, requestedCurrency).get(requestedCurrency);
        return price.multiply(rate);
    }
}
