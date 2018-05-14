package de.htwg.swqs.order.shippingcost;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ShippingCostServiceImpl implements ShippingCostService {

  private static final String DEFAULT_COUNTRY = "DE";
  private static final BigDecimal FREE_SHIPPING_START = new BigDecimal("200.00");
  private static final int WEIGHT_PER_ITEM_IN_GRAMS = 300;


  /**
   * Calculates the shipping costs for a given list of items and a CustomerInfo. If the
   * customer lives outside of the default country, an additional charge for international shipping
   * will be added.
   *
   * @param customerInfo The info about the name and address of the customer
   * @param orderItems A list with the ordered ShoppingCartItems
   * @return A BigDecimal containing the shipping costs
   */
  public BigDecimal calculateShippingCosts(CustomerInfo customerInfo, List<OrderItem> orderItems,
      BigDecimal totalCostOfItems) {

    if (orderItems.isEmpty()) {
      throw new IllegalArgumentException(
          "Cannot calculate shipping costs for empty order item list!");
    }

    if (totalCostOfItems.compareTo(FREE_SHIPPING_START) >= 0) {
      // the customer must not pay shipping costs
      return new BigDecimal("0.00");
    }

    // fail: we have to summerize the amount of each item
    int amountOfItems = orderItems.size();
    int weightOfItems = amountOfItems * WEIGHT_PER_ITEM_IN_GRAMS;

    // thats the correct way:
    weightOfItems = calculateWeightForItemList(orderItems);

    // check if it is a international order
    BigDecimal internationalShippingExtraCost =
        customerInfo.getIsoCountryCode().equals(DEFAULT_COUNTRY) ? new BigDecimal("0")
            : new BigDecimal("10.99");

    BigDecimal shippingCost;

    // costs are taken from the dhl price list for 'Paket' delivery
    if (weightOfItems <= 2000) {
      shippingCost = new BigDecimal("4.99");
    } else if (weightOfItems <= 5000) {
      shippingCost = new BigDecimal("5.99");
    } else if (weightOfItems <= 10000) {
      shippingCost = new BigDecimal("8.49");
    } else if (weightOfItems <= 31500) {
      shippingCost = new BigDecimal("16.49");
    } else {
      // not supported by dhl, we need a custom delivery
      shippingCost = new BigDecimal("50.00");
    }

    return internationalShippingExtraCost.add(shippingCost);
  }

  private int calculateWeightForItemList(List<OrderItem> itemList) {
    int weightTotal = 0;
    for (OrderItem item : itemList) {
      weightTotal += item.getQuantity() * WEIGHT_PER_ITEM_IN_GRAMS;
    }
    return weightTotal;
  }


}
