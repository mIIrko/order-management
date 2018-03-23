package de.htwg.swqs.order.model;

import de.htwg.swqs.order.payment.PaymentMethod;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "order_data")
public class Order {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    private CustomerInfo customerInfo;
    @OneToOne
    private ShoppingCart shoppingCart;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount_shipping")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency_shipping"))
    })
    private Cost costShipping;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount_total")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency_total"))
    })
    private Cost costTotal;
    private LocalDate orderDate;
    private PaymentMethod paymentMethod;

    public Order() {
    }

    public Order(CustomerInfo customerInfo, ShoppingCart shoppingCart, Cost costShipping, Cost costTotal, LocalDate orderDate, PaymentMethod paymentMethod) {
        this.customerInfo = customerInfo;
        this.shoppingCart = shoppingCart;
        this.costShipping = costShipping;
        this.costTotal = costTotal;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
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

    public Cost getCostShipping() {
        return costShipping;
    }

    public void setCostShipping(Cost costShipping) {
        this.costShipping = costShipping;
    }

    public Cost getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(Cost costTotal) {
        this.costTotal = costTotal;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
