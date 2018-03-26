package de.htwg.swqs.order.util;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.ShoppingCart;

/**
 * Dummy wrapper class to hold two nested objects.
 * Is needed for automatically mapping the json requestbody to java objects
 */
public class RequestWrapper {

    private CustomerInfo customerInfo;
    private ShoppingCart shoppingCart;

    public RequestWrapper(){}

    public RequestWrapper(CustomerInfo customerInfo, ShoppingCart shoppingCart) {
        this.customerInfo = customerInfo;
        this.shoppingCart = shoppingCart;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
