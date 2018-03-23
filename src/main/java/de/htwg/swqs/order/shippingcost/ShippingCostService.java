package de.htwg.swqs.order.shippingcost;

import de.htwg.swqs.order.model.Cost;
import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public interface ShippingCostService {

    Cost calculateShippingCosts(CustomerInfo customerInfo, ShoppingCart shoppingCart);

}
