package de.htwg.swqs.order.shippingcost;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.OrderItem;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ShippingCostServiceTest {

  private ShippingCostService shippingCostService;

  @Before
  public void setUp() {
    this.shippingCostService = new ShippingCostServiceImpl();
  }

  @Test(expected = IllegalArgumentException.class)
  public void calculateShippingCostsWithEmptyListShouldThrowError() {
    // setup
    List<OrderItem> list = new ArrayList<>();
    CustomerInfo customerInfo = createDummyCustomerInfo();
    customerInfo.setIsoCountryCode("DE");

    // execute
    BigDecimal costs = this.shippingCostService
        .calculateShippingCosts(customerInfo, list, new BigDecimal("42.75"));

    // The test is verified by the expected exception
  }

  @Test
  public void calculateShippingCostsWithInternationalShipping() {
    // setup
    List<OrderItem> list = Arrays.asList(
        new OrderItem(2, 10001L, new BigDecimal("3.33")),
        new OrderItem(1, 10002L, new BigDecimal("4.23")),
        new OrderItem(5, 10003L, new BigDecimal("0.99"))
    );
    BigDecimal costTotal = new BigDecimal(3.33 * 2 + 4.23 * 1 + 0.99 * 5);
    // weightTotal = (2 + 1 + 5) * 300; in grams = 2400g -> 5.99 shipping costs

    CustomerInfo customerInfo = createDummyCustomerInfo();
    customerInfo.setIsoCountryCode("AT");

    // execute
    BigDecimal calculatedShippingCosts = this.shippingCostService
        .calculateShippingCosts(customerInfo, list, costTotal);

    // verify
    assertEquals(new BigDecimal("16.98"), calculatedShippingCosts);
  }

  private CustomerInfo createDummyCustomerInfo() {
    CustomerInfo c = new CustomerInfo();
    c.setEmail("max@muster.de");
    c.setFirstname("Max");
    c.setSurname("Muster");
    c.setStreet("Musterstraße 3");
    c.setCity("Musterhausen");
    c.setPostcode("78457");
    c.setIsoCountryCode("DE");
    return c;
  }
}
