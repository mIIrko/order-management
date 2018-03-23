package de.htwg.swqs.order.controller;

import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.service.OrderService;
import de.htwg.swqs.order.util.RequestObject;
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

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Order createAndPersistNewOrder(@RequestBody RequestObject requestObject, @RequestParam(value = "currency", required = true) Currency currency) {

        Order createdOrder = this.orderService.createOrder(requestObject.getCustomerInfo(), requestObject.getShoppingCart());
        return this.orderService.persistOrder(createdOrder);
    }
}
