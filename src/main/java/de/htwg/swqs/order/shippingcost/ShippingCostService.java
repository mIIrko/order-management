package de.htwg.swqs.order.shippingcost;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public interface ShippingCostService {

  BigDecimal calculateShippingCosts(CustomerInfo customerInfo, List<OrderItem> orderItems);

}
