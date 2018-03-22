package de.htwg.swqs.order.shipping;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.ShoppingCart;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface ShippingService {

    public BigDecimal getShippingCost(CustomerInfo customerInfo, ShoppingCart shoppingCart);

    public BigDecimal initShippingProvider();
}
