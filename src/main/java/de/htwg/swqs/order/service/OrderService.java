package de.htwg.swqs.order.service;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.Currency;

@Service
public interface OrderService {

    Order createOrder(CustomerInfo customerInfo, ShoppingCart shoppingCart, Currency currency);

    Order persistOrder(Order order);

    Order getOrderById(long id);

}
