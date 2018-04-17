package de.htwg.swqs.order.service;

import de.htwg.swqs.order.mail.EmailService;
import de.htwg.swqs.order.model.Cost;
import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.OrderItem;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import de.htwg.swqs.order.util.OrderNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  private static final BigDecimal FREE_SHIPPING_START = new BigDecimal("200.00");
  private static final Currency DEFAULT_CURRENCY = Currency.getInstance("EUR");

  private ShippingCostService shippingCostService;
  private OrderRepository orderRepository;
  private CurrencyConverterService currencyConverterService;
  private EmailService emailService;

  /**
   * Constructor with the depended-on components; used for the dependency injection by spring.
   */
  @Autowired
  public OrderServiceImpl(
      ShippingCostService shippingCostService,
      OrderRepository orderRepository,
      CurrencyConverterService currencyConverterService,
      EmailService emailService) {
    this.shippingCostService = shippingCostService;
    this.orderRepository = orderRepository;
    this.currencyConverterService = currencyConverterService;
    this.emailService = emailService;
  }

  /**
   * Creates a new order from the customer info and the corresponding shopping cart; the currency
   * object is the one chosen from the customer.
   *
   * @param customerInfo The info about customer name and address
   * @param orderItems The shopping cart with the ShoppingCartItems
   * @return The created order object
   */
  public Order createOrder(CustomerInfo customerInfo, List<OrderItem> orderItems,
      Currency currency) {
    Order newOrder = new Order();
    newOrder.setCustomerInfo(customerInfo);
    newOrder.setShoppingCart(orderItems);
    newOrder.setOrderDate(LocalDate.now());

    // calculate total cost of items
    BigDecimal totalCostOfItems = new BigDecimal("0.00");
    for (OrderItem item : orderItems) {
      totalCostOfItems = totalCostOfItems
          .add(item.getPriceEuro().multiply(new BigDecimal(item.getQuantity())));
    }

    // calculate shipping costs
    BigDecimal shippingCosts = new BigDecimal("0.00");
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
      try {
        BigDecimal exchangedCosts = this.currencyConverterService
            .convertTo(DEFAULT_CURRENCY, currency, totalCosts);
        newOrder.setCostTotal(new Cost(exchangedCosts, currency));
      } catch (IOException ioe) {
        // in case the exchange service does not work we fulfill the order with our default currency
        newOrder.setCostTotal(new Cost(totalCosts, DEFAULT_CURRENCY));
      }
    } else {
      newOrder.setCostTotal(new Cost(totalCosts, currency));
    }

    // create and save the order
    Order createdOrder = this.orderRepository.saveAndFlush(newOrder);

    // send email to the customer
    this.emailService.sendMail(customerInfo.getEmail(), "Your order " + createdOrder.getId(),
        "Thank you for your order!");

    return createdOrder;
  }

  /**
   * Returns the requested order, identified by the id.
   *
   * @param id The id of the wanted order
   * @return The full complete order object
   * @throws OrderNotFoundException If there's no order with this id
   */
  public Order getOrderById(long id) throws OrderNotFoundException {
    Optional<Order> orderOptional = this.orderRepository.findById(id);
    if (!orderOptional.isPresent()) {
      throw new OrderNotFoundException("Order with id " + id + " does not exist");
    }
    return orderOptional.get();
  }
}
