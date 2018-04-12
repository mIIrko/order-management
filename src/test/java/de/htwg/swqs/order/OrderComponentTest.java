package de.htwg.swqs.order;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.OrderItem;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.service.OrderService;
import de.htwg.swqs.order.service.OrderServiceImpl;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import de.htwg.swqs.order.util.OrderNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderConfiguration.class)
@DataJpaTest
public class OrderComponentTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ShippingCostService shippingCostService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CurrencyConverterService currencyConverterService;

    private OrderService orderService;

    @Before
    public void setUp() {
        // setup
        this.orderService = new OrderServiceImpl(
                this.shippingCostService,
                this.orderRepository,
                this.currencyConverterService
        );
    }

    @Test
    public void createNewNationalOrderAndCheckIfPersisted() throws OrderNotFoundException {
        // setup
        List<OrderItem> list = Arrays.asList(
                createDummyOrderItem(1L, 2, new BigDecimal("5.00")),
                createDummyOrderItem(2L, 3, new BigDecimal("2.99"))
        );

        list.forEach(item -> System.out.println(item.getProductId()));

        // execute
        Order createdOrder = this.orderService.createOrder(createDummyCustomerInfo("DE"), list, Currency.getInstance("EUR"));
        Order retrievedOrder = this.orderService.getOrderById(createdOrder.getId());

        System.out.println(retrievedOrder.toString());

        // verify
        assertEquals(createdOrder, retrievedOrder);
    }

    @Test
    public void createNewNationalOrderWithFreeShippingAndCheckIfPersisted() throws OrderNotFoundException {
        // setup
        List<OrderItem> list = Arrays.asList(
                createDummyOrderItem(1L, 12, new BigDecimal("50.00")),
                createDummyOrderItem(2L, 3, new BigDecimal("2.99"))
        );

        list.forEach(item -> System.out.println(item.getProductId()));

        // execute
        Order createdOrder = this.orderService.createOrder(createDummyCustomerInfo("DE"), list, Currency.getInstance("EUR"));
        Order retrievedOrder = this.orderService.getOrderById(createdOrder.getId());

        System.out.println(retrievedOrder.toString());

        // verify
        assertEquals(createdOrder, retrievedOrder);
    }

    @Test
    public void createNewInternationalOrderAndCheckIfPersisted() throws OrderNotFoundException {
        // setup
        List<OrderItem> list = Arrays.asList(
                createDummyOrderItem(1L, 2, new BigDecimal("5.00")),
                createDummyOrderItem(2L, 3, new BigDecimal("2.99"))
        );

        list.forEach(item -> System.out.println(item.getProductId()));

        // execute
        Order createdOrder = this.orderService.createOrder(createDummyCustomerInfo("US"), list, Currency.getInstance("USD"));
        Order retrievedOrder = this.orderService.getOrderById(createdOrder.getId());

        System.out.println(retrievedOrder.toString());

        // verify
        assertEquals(createdOrder, retrievedOrder);
    }

    @Test
    public void createNewInternationalOrderWithFreeShippingAndCheckIfPersisted() throws OrderNotFoundException {
        // setup
        List<OrderItem> list = Arrays.asList(
                createDummyOrderItem(1L, 12, new BigDecimal("50.00")),
                createDummyOrderItem(2L, 3, new BigDecimal("2.99"))
        );

        list.forEach(item -> System.out.println(item.getProductId()));

        // execute
        Order createdOrder = this.orderService.createOrder(createDummyCustomerInfo("US"), list, Currency.getInstance("USD"));
        Order retrievedOrder = this.orderService.getOrderById(createdOrder.getId());

        System.out.println(retrievedOrder.toString());

        // verify
        assertEquals(createdOrder, retrievedOrder);
    }

    @Test(expected = OrderNotFoundException.class)
    public void tryToGetNotExistingOrder() throws OrderNotFoundException {
        // no setup, we need no entries in the db

        // execute
        Order retrievedOrder = this.orderService.getOrderById(1L);
    }



    private CustomerInfo createDummyCustomerInfo(String isoCountryCode) {
        CustomerInfo c = new CustomerInfo();
        c.setSurname("Mustermann");
        c.setFirstname("Max");
        c.setStreet("Hauptstraße 3");
        c.setCity("Konstanz");
        c.setPostcode("78467");
        c.setIsoCountryCode(isoCountryCode);
        return c;
    }

    private OrderItem createDummyOrderItem(long productId, int productAmount, BigDecimal price) {
        OrderItem s = new OrderItem();
        s.setQuantity(productAmount);
        s.setProductId(productId);
        s.setPriceEuro(price);
        return s;
    }
}
