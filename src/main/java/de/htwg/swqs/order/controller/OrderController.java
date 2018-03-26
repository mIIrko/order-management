package de.htwg.swqs.order.controller;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.ShoppingCart;
import de.htwg.swqs.order.service.OrderService;
import de.htwg.swqs.order.util.CurrencyWrongException;
import de.htwg.swqs.order.util.RequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable long id) {
        return this.orderService.getOrderById(id);
    }

    /**
     * Creates new orders from customer info and shopping cart, listens on following url:
     * e.g. .../orders/create?currency=eur
     *
     * @param requestWrapper The payload with customer info and shopping cart
     * @param currencyCode  The ISO-4217 currency code
     * @return              The created order
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Order createAndPersistNewOrder(@RequestBody RequestWrapper requestWrapper, @RequestParam(value = "currency") String currencyCode) {

        // todo: currency and payment method in the requestwrapper
        Currency currency;
        try {
            currency = Currency.getInstance(currencyCode.toUpperCase());
        } catch (IllegalArgumentException iae) {
          throw new CurrencyWrongException("Wrong currency code: '" + currencyCode + "' is not a valid currency");
        }

        Order createdOrder = this.orderService.createOrder(requestWrapper.getCustomerInfo(), requestWrapper.getShoppingCart(), currency);
        return this.orderService.persistOrder(createdOrder);
    }

    @PostMapping("/test-customer-info")
    public CustomerInfo testCreatingCustomerInfo(@RequestBody CustomerInfo customerInfo) {
        System.out.println(customerInfo.toString());
        return customerInfo;
    }

    @PostMapping("/test-shopping-cart")
    public ShoppingCart testCreatingCustomerInfo(@RequestBody ShoppingCart shoppingCart) {
        System.out.println(shoppingCart.toString());
        return shoppingCart;
    }
}
