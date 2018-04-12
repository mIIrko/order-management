package de.htwg.swqs.order.service;

import de.htwg.swqs.order.model.*;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import de.htwg.swqs.order.util.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal FREE_SHIPPING_START = new BigDecimal("200.00");
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("EUR");

    private ShippingCostService shippingCostService;
    private OrderRepository orderRepository;
    private CurrencyConverterService currencyConverterService;

    @Autowired
    public OrderServiceImpl(ShippingCostService shippingCostService, OrderRepository orderRepository, CurrencyConverterService currencyConverterService) {
        this.shippingCostService = shippingCostService;
        this.orderRepository = orderRepository;
        this.currencyConverterService = currencyConverterService;
    }

    /**
     * Creates a new order from the customer info and the corresponding shopping cart; the currency object is the
     * one chosen from the customer
     *
     * @param customerInfo The info about customer name and address
     * @param orderItems The shopping cart with the ShoppingCartItems
     * @return The created order object
     */
    public Order createOrder(CustomerInfo customerInfo, List<OrderItem> orderItems, Currency currency) {
        Order newOrder = new Order();
        newOrder.setCustomerInfo(customerInfo);
        newOrder.setShoppingCart(orderItems);
        newOrder.setOrderDate(LocalDate.now());

        BigDecimal totalCostOfItems = new BigDecimal("0.00");
        BigDecimal shippingCosts = new BigDecimal("0.00");

        for (OrderItem item : orderItems) {
            totalCostOfItems = totalCostOfItems.add(item.getPriceEuro().multiply(new BigDecimal(item.getQuantity())));
        }
        if (totalCostOfItems.compareTo(FREE_SHIPPING_START) >= 0) {
            // the customer must not pay shipping costs
            newOrder.setCostShipping(new BigDecimal("0.00"));
        } else {
            // shipping costs delivered always as euro
            shippingCosts = this.shippingCostService.calculateShippingCosts(customerInfo, orderItems);
            newOrder.setCostShipping(shippingCosts);
        }

        BigDecimal totalCosts = totalCostOfItems.add(shippingCosts);

        // check if the customer wants another currency than euro
        if (!currency.equals(DEFAULT_CURRENCY)) {
            // the customer want the total costs in another currency than euro,
            // we must use the currency exchange service
            BigDecimal exchangedCosts = this.currencyConverterService.convertTo(DEFAULT_CURRENCY, currency, totalCosts);
            newOrder.setCostTotal(new Cost(exchangedCosts, currency));
        } else {
            newOrder.setCostTotal(new Cost(totalCosts, currency));
        }
        return this.orderRepository.saveAndFlush(newOrder);
    }


    /**
     * Returns the requested order, identified by the id
     *
     * @param id    The id of the wanted order
     * @return      The full complete order object
     * @throws OrderNotFoundException   If there's no order with this id
     */
    public Order getOrderById(long id) throws OrderNotFoundException {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            throw new OrderNotFoundException("Order with id " + id + " does not exist");
        }
        return orderOptional.get();
    }
}
