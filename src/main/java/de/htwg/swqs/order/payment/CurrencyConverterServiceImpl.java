package de.htwg.swqs.order.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private static final String MAIN_URL = "example.org";

    @Override
    public BigDecimal convertTo(Currency currencyFrom, Currency currencyTo, BigDecimal amount) {

        if (currencyFrom.equals(currencyTo)) {
            return amount;
        }

        if (amount.compareTo(new BigDecimal("0.00")) < 1) {
            // the amount is 0 or negative
            throw new IllegalArgumentException("The passed amount must be greater than 0");
        }

        return amount.multiply(new BigDecimal("1.5"));
    }
}
