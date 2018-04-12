package de.htwg.swqs.order.shippingcost;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.OrderItem;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ShippingCostServiceTest {

    private ShippingCostService shippingCostService;

    @Before
    public void setUp() {
        this.shippingCostService = new ShippingCostServiceImpl();
    }

    @Test
    public void calculateShippingCostsWithEmptyList() {
        // setup
        List<OrderItem> list = new ArrayList<>();
        CustomerInfo customerInfo = mock(CustomerInfo.class);
        when(customerInfo.getIsoCountryCode()).thenReturn("DE");

        // execute
        BigDecimal costs = this.shippingCostService.calculateShippingCosts(customerInfo,list);

        // verify the calculated shipping cost is greater than zero
        assertEquals(1, (costs.compareTo(new BigDecimal("0.00"))));

    }

    @Test
    public void calculateShippingCostsWithInternationalShipping() {
        // setup
        List<OrderItem> list = new ArrayList<>();
        CustomerInfo customerInfo = mock(CustomerInfo.class);
        when(customerInfo.getIsoCountryCode()).thenReturn("AT");

        // execute
        BigDecimal costs = this.shippingCostService.calculateShippingCosts(customerInfo,list);

        // verify
        fail("Verification is missing");
    }

}
