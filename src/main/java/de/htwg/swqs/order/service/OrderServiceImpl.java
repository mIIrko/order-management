package de.htwg.swqs.order.service;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.ShoppingCart;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.shipping.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;


public class OrderServiceImpl implements OrderService {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order processOrder(CustomerInfo customerInfo, ShoppingCart shoppingCart) {
        Order newOrder = new Order();
        newOrder.setCustomerInfo(customerInfo);
        newOrder.setShoppingCart(shoppingCart);
        newOrder.setOrderDate(LocalDate.now());
        BigDecimal shippingCosts = this.shippingService.getShippingCost(customerInfo, shoppingCart);
        newOrder.setCostShipping(shippingCosts);
        newOrder.setCostTotal(shippingCosts.add(shoppingCart.getCartTotalSum(), new MathContext(2, RoundingMode.HALF_EVEN)));

        orderRepository.saveAndFlush(newOrder);

        return newOrder;
    }
}
