package de.htwg.swqs.order.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.htwg.swqs.order.mail.EmailService;
import de.htwg.swqs.order.model.Cost;
import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.OrderItem;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.payment.PaymentMethod;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import de.htwg.swqs.order.util.OrderNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * Example of a hybrid fixture creation. The creation of customerInfo and shoppingCart is delegated
 * to separate helper functions, the class variables orderService, orderRepository and
 * shippingCostService are created by the init() function
 */
public class OrderServiceHybridFixtureTest {

  private OrderService orderService;
  private ShippingCostService shippingCostService;
  private OrderRepository orderRepository;
  private CurrencyConverterService currencyConverterService;
  private EmailService emailService;

  @Before
  public void init() {
    this.shippingCostService = mock(ShippingCostService.class);
    this.orderRepository = mock(OrderRepository.class);
    this.currencyConverterService = mock(CurrencyConverterService.class);
    this.emailService = mock(EmailService.class);
    this.orderService = new OrderServiceImpl(this.shippingCostService, this.orderRepository,
        this.currencyConverterService, this.emailService);
  }

  private CustomerInfo createDummyCustomerInfo() {
    CustomerInfo customerInfo = new CustomerInfo();
    customerInfo.setEmail("max@muster.de");
    customerInfo.setCity("Konstanz");
    customerInfo.setPostcode("78467");
    customerInfo.setStreet("Hauptstraße 3");
    customerInfo.setIsoCountryCode("DE");
    customerInfo.setFirstname("Max");
    customerInfo.setSurname("Muster");
    return customerInfo;
  }

  private List<OrderItem> createDummyOrderItemList() {
    OrderItem firstItem = new OrderItem();
    firstItem.setProductId(1L);
    firstItem.setPriceEuro(new BigDecimal("3.33"));
    firstItem.setQuantity(3);
    OrderItem secondItem = new OrderItem();
    secondItem.setProductId(1L);
    secondItem.setPriceEuro(new BigDecimal("6.99"));
    secondItem.setQuantity(6);
    return Arrays.asList(firstItem, secondItem);
  }

  @Test
  public void createNewOrderObjectAndCheckRepoIsCalled() {
    // setup
    CustomerInfo customerInfo = createDummyCustomerInfo();
    List<OrderItem> itemList = createDummyOrderItemList();
    Currency currency = Currency.getInstance("EUR");

    when(shippingCostService
        .calculateShippingCosts(eq(customerInfo), eq(itemList), any(BigDecimal.class)))
        .thenReturn(
            new BigDecimal("4.20")
        );

    Order dummyOrder = mock(Order.class);
    when(dummyOrder.getId()).thenReturn(1L);
    when(this.orderRepository.saveAndFlush(any(Order.class))).thenReturn(dummyOrder);

    // execute
    Order createdOrder = this.orderService.createOrder(customerInfo, itemList, currency);
    Order persistedOrder = this.orderService.persistOrder(createdOrder);

    // verify
    verify(this.orderRepository, times(1)).saveAndFlush(any(Order.class));
    verifyNoMoreInteractions(this.orderRepository);
  }

  @Test
  public void getOrderByIdSuccessful() throws OrderNotFoundException {
    // setup
    CustomerInfo customerInfo = createDummyCustomerInfo();
    List<OrderItem> itemList = createDummyOrderItemList();
    Currency currency = Currency.getInstance("EUR");
    long testId = 1L;
    Order testOrder = new Order(customerInfo, itemList, new BigDecimal("4.20"),
        new Cost(new BigDecimal("51.93"), currency), LocalDate.now(), PaymentMethod.prePayment);
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
    CustomerInfo customerInfo = createDummyCustomerInfo();
    List<OrderItem> itemList = createDummyOrderItemList();
    Currency currency = Currency.getInstance("EUR");

    long testId = 1L;
    Order testOrder = new Order(customerInfo, itemList, new BigDecimal("4.20"),
        new Cost(new BigDecimal("51.93"), currency), LocalDate.now(), PaymentMethod.prePayment);
    testOrder.setId(testId);
    when(this.orderRepository.findById(testId)).thenReturn(Optional.empty());

    // execute
    Order returnedOrder = this.orderService.getOrderById(testId);

    // The test is verified by the expected exception
  }

}
