package de.htwg.swqs.order.util;

public class CurrencyWrongException extends RuntimeException{

    public CurrencyWrongException() {
        super();
    }
    public CurrencyWrongException(String message, Throwable cause) {
        super(message, cause);
    }
    public CurrencyWrongException(String message) {
        super(message);
    }
    public CurrencyWrongException(Throwable cause) {
        super(cause);
    }
}
