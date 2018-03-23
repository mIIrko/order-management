package de.htwg.swqs.order.shippingcost;

import de.htwg.swqs.order.model.Cost;
import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.ShoppingCart;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
public class ShippingCostServiceImpl implements ShippingCostService{

    public Cost calculateShippingCosts(CustomerInfo customerInfo, ShoppingCart shoppingCart) {
        return new Cost(new BigDecimal("4.20"), Currency.getInstance("EUR"));
    }
}
