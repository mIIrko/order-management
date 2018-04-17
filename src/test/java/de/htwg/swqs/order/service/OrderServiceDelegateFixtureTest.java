package de.htwg.swqs.order.service;

import de.htwg.swqs.order.mail.EmailService;
import de.htwg.swqs.order.mail.EmailServiceImpl;
import de.htwg.swqs.order.model.*;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.payment.PaymentMethod;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import de.htwg.swqs.order.util.OrderNotFoundException;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Example of a delegated fixture creation.
 * The creation of customerInfo and shoppingCart is delegated to separate helper functions.
 *
 */
public class OrderServiceDelegateFixtureTest {


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
        ShippingCostService shippingCostServiceMock = mock(ShippingCostService.class);
        when(shippingCostServiceMock.calculateShippingCosts(customerInfo, itemList)).thenReturn(
                new BigDecimal("4.20")
        );
        OrderRepository orderRepositoryMock = mock(OrderRepository.class);
        Order dummyOrder = mock(Order.class);
        when(dummyOrder.getId()).thenReturn(1L);
        when(orderRepositoryMock.saveAndFlush(any(Order.class))).thenReturn(dummyOrder);

        CurrencyConverterService currencyConverterServiceMock = mock(CurrencyConverterService.class);
        // EmailService emailServiceMock = mock(EmailService.class);
        OrderService orderService = new OrderServiceImpl(shippingCostServiceMock, orderRepositoryMock, currencyConverterServiceMock, new EmailServiceImpl());
        // execute
        Order createdOrder = orderService.createOrder(customerInfo, itemList, currency);
        // verify
       verify(orderRepositoryMock, times(1)).saveAndFlush(any(Order.class));
       verifyNoMoreInteractions(orderRepositoryMock);
    }

    @Test
    public void getOrderByIdSuccessful() throws OrderNotFoundException{
        // setup
        CustomerInfo customerInfo = createDummyCustomerInfo();
        List<OrderItem> itemList = createDummyOrderItemList();
        Currency currency = Currency.getInstance("EUR");
        ShippingCostService shippingCostServiceMock = mock(ShippingCostService.class);
        OrderRepository orderRepositoryMock = mock(OrderRepository.class);
        long testId = 1L;
        Order testOrder = new Order(customerInfo, itemList, new BigDecimal("4.20"), new Cost(new BigDecimal("51.93"), currency), LocalDate.now(), PaymentMethod.prePayment);
        testOrder.setId(testId);
        when(orderRepositoryMock.findById(testId)).thenReturn(Optional.of(testOrder));
        CurrencyConverterService currencyConverterServiceMock = mock(CurrencyConverterService.class);
        EmailService emailServiceMock = mock(EmailService.class);
        OrderService orderService = new OrderServiceImpl(shippingCostServiceMock, orderRepositoryMock, currencyConverterServiceMock, emailServiceMock);        // execute
        Order returnedOrder = orderService.getOrderById(testId);

        // verify
        assertEquals(testOrder, returnedOrder);
    }

    @Test(expected = OrderNotFoundException.class)
    public void getOrderByIdWhoDoesNotExistThrowException() throws OrderNotFoundException {
        // setup
        CustomerInfo customerInfo = createDummyCustomerInfo();
        List<OrderItem> itemList = createDummyOrderItemList();
        Currency currency = Currency.getInstance("EUR");
        ShippingCostService shippingCostServiceMock = mock(ShippingCostService.class);
        OrderRepository orderRepositoryMock = mock(OrderRepository.class);
        long testId = 1L;
        Order testOrder = new Order(customerInfo, itemList, new BigDecimal("4.20"), new Cost(new BigDecimal("51.13"), currency), LocalDate.now(), PaymentMethod.prePayment);
        testOrder.setId(testId);
        when(orderRepositoryMock.findById(testId)).thenReturn(Optional.empty());
        CurrencyConverterService currencyConverterServiceMock = mock(CurrencyConverterService.class);
        EmailService emailServiceMock = mock(EmailService.class);
        OrderService orderService = new OrderServiceImpl(shippingCostServiceMock, orderRepositoryMock, currencyConverterServiceMock, emailServiceMock);        // execute
        Order returnedOrder = orderService.getOrderById(testId);

        // verify
    }
}
