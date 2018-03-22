package de.htwg.swqs.order.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Order {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    private CustomerInfo customerInfo;
    @OneToOne
    private ShoppingCart shoppingCart;
    private BigDecimal costShipping;
    private BigDecimal costTotal;
    private LocalDate orderDate;

    public Order() {
    }

    public Order(CustomerInfo customerInfo, ShoppingCart shoppingCart, BigDecimal costShipping, BigDecimal costTotal, LocalDate orderDate) {
        this.customerInfo = customerInfo;
        this.shoppingCart = shoppingCart;
        this.costShipping = costShipping;
        this.costTotal = costTotal;
        this.orderDate = orderDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BigDecimal getCostShipping() {
        return costShipping;
    }

    public void setCostShipping(BigDecimal costShipping) {
        this.costShipping = costShipping;
    }

    public BigDecimal getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(BigDecimal costTotal) {
        this.costTotal = costTotal;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
