package de.htwg.swqs.order.service;

import de.htwg.swqs.order.model.*;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    private CustomerInfo customerInfo;
    private ShoppingCart shoppingCart;

    @Before
    public void init() {
        // create new example CustomerInfo
        customerInfo = new CustomerInfo();
        customerInfo.setCity("Konstanz");
        customerInfo.setPostcode("78467");
        customerInfo.setStreet("Hauptstraße 3");
        customerInfo.setIsoCountryCode("DE");
        customerInfo.setFirstname("Max");
        customerInfo.setSurname("Muster");

        // create new example ShoppingCart
        shoppingCart = new ShoppingCart();
        ShoppingCartItem firstItem = new ShoppingCartItem();
        firstItem.setProduct(new Product(1, "Product 1", "Description 1", new BigDecimal("3.33")));
        firstItem.setQuantity(3);
        ShoppingCartItem secondItem = new ShoppingCartItem();
        secondItem.setProduct(new Product(2, "Product 2", "Description 2", new BigDecimal("6.99")));
        secondItem.setQuantity(6);
        List<ShoppingCartItem> itemList = Arrays.asList(firstItem, secondItem);
        shoppingCart.setItemsInShoppingCartAsList(itemList);

        BigDecimal totalSum = new BigDecimal("0");
        for (ShoppingCartItem item : itemList) {
            BigDecimal subTotal = item.getProduct().getPriceEuro().multiply(new BigDecimal(item.getQuantity()));
            totalSum = totalSum.add(subTotal);
        }
        System.out.println("Cart total sum = " + totalSum.toString());
        shoppingCart.setCartTotalSum(totalSum);
    }

    /**
     * Mock the OrderRepository and the ShippingCostService to test the OrderServiceImpl     */
    @Test
    public void createNewOrderObject() {
        // setup
        ShippingCostService shippingCostServiceMock = mock(ShippingCostService.class);
        when(shippingCostServiceMock.calculateShippingCosts(this.customerInfo, this.shoppingCart)).thenReturn(
                new Cost("4.20", Currency.getInstance("EUR"))
        );

        OrderRepository orderRepositoryMock = mock(OrderRepository.class);
        OrderService orderService = new OrderServiceImpl(shippingCostServiceMock, orderRepositoryMock);

        // execute
        Order createdOrder = orderService.createOrder(this.customerInfo, this.shoppingCart);

        // verify
        assertEquals(createdOrder.getCustomerInfo(), this.customerInfo);
        assertEquals(createdOrder.getShoppingCart(), this.shoppingCart);
        assertEquals(createdOrder.getOrderDate(), LocalDate.now());
        assertEquals(BigDecimal.valueOf((3.33*3) + (6.99*6) + 4.20),createdOrder.getCostTotal().getAmount());
    }
}
