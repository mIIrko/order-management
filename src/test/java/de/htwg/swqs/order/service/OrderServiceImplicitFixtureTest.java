package de.htwg.swqs.order.service;

import de.htwg.swqs.order.mail.EmailService;
import de.htwg.swqs.order.model.*;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.payment.PaymentMethod;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import de.htwg.swqs.order.util.OrderNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Example of a implicit fixture creation. The whole fixture is created by the init() function,
 * called before every test.
 */
public class OrderServiceImplicitFixtureTest {

  private OrderService orderService;
  private ShippingCostService shippingCostService;
  private OrderRepository orderRepository;
  private EmailService emailService;
  private CurrencyConverterService currencyConverterService;


  private CustomerInfo customerInfo;
  private Currency currency;

  private List<OrderItem> itemList;

  @Before
  public void init() {
    // create new example customer info
    this.customerInfo = new CustomerInfo();
    this.customerInfo.setEmail("max@muster.de");
    this.customerInfo.setCity("Konstanz");
    this.customerInfo.setPostcode("78467");
    this.customerInfo.setStreet("Hauptstraße 3");
    this.customerInfo.setIsoCountryCode("DE");
    this.customerInfo.setFirstname("Max");
    this.customerInfo.setSurname("Muster");

    // create order item list
    OrderItem firstItem = new OrderItem();
    firstItem.setProductId(1L);
    firstItem.setPriceEuro(new BigDecimal("3.33"));
    firstItem.setQuantity(3);
    OrderItem secondItem = new OrderItem();
    secondItem.setProductId(1L);
    secondItem.setPriceEuro(new BigDecimal("6.99"));
    secondItem.setQuantity(6);
    this.itemList = Arrays.asList(firstItem, secondItem);

    // create new example currency
    this.currency = Currency.getInstance("EUR");

    this.shippingCostService = mock(ShippingCostService.class);
    this.orderRepository = mock(OrderRepository.class);
    this.currencyConverterService = mock(CurrencyConverterService.class);
    this.emailService = mock(EmailService.class);
    this.orderService = new OrderServiceImpl(this.shippingCostService, this.orderRepository,
        this.currencyConverterService, this.emailService);
  }

  @Test
  public void createNewOrderObjectAndCheckRepoIsCalled() {

    // setup
    when(this.shippingCostService
        .calculateShippingCosts(eq(this.customerInfo), eq(this.itemList), any(BigDecimal.class)))
        .thenReturn(
            new BigDecimal("4.20")
        );
    Order dummyOrder = mock(Order.class);
    when(dummyOrder.getId()).thenReturn(1L);
    when(this.orderRepository.saveAndFlush(any(Order.class))).thenReturn(dummyOrder);

    // execute
    Order createdOrder = orderService.createOrder(this.customerInfo, this.itemList, this.currency);
    Order persistedOrder = orderService.persistOrder(createdOrder);

    // verify
    verify(this.orderRepository, times(1)).saveAndFlush(any(Order.class));
    verifyNoMoreInteractions(this.orderRepository);
  }

  @Test
  public void getOrderByIdSuccessful() throws OrderNotFoundException {
    // setup
    long testId = 1L;

    Order testOrder = new Order(
        this.customerInfo,
        this.itemList,
        new BigDecimal("4.20"),
        new Cost(new BigDecimal("51.93"), this.currency),
        LocalDate.now(),
        PaymentMethod.prePayment
    );
    testOrder.setId(testId);
    when(this.orderRepository.findById(testId)).thenReturn(Optional.of(testOrder));

    // execute
    Order returnedOrder = this.orderService.getOrderById(testId);

    // verify
    assertEquals(testOrder, returnedOrder);
  }

  @Test(expected = OrderNotFoundException.class)
  public void getOrderByIdWhoDoesNotExistThrowException() throws OrderNotFoundException {
    // setup
    long testId = 1L;

    Order testOrder = new Order(
        this.customerInfo,
        this.itemList,
        new BigDecimal("4.20"),
        new Cost(new BigDecimal("51.13"), this.currency),
        LocalDate.now(),
        PaymentMethod.prePayment
    );
    testOrder.setId(testId);
    when(this.orderRepository.findById(testId)).thenReturn(Optional.empty());

    // execute
    Order returnedOrder = this.orderService.getOrderById(testId);

    // verify
  }
}
