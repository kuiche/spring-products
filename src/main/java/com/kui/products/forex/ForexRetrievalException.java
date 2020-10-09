package com.kui.products.forex;

public class ForexRetrievalException extends Exception {
    public ForexRetrievalException(String message) {
        super(message);
    }

    public ForexRetrievalException(String message, Exception e) {
        super(message, e);
    }
}
