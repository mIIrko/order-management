package de.htwg.swqs.order.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
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
