package de.htwg.swqs.order.model;

import de.htwg.swqs.order.payment.PaymentMethod;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "order_data")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private CustomerInfo customerInfo;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems;
    @Column(precision = 19, scale = 4)
    private BigDecimal costShipping;
    @Embedded
    private Cost costTotal;
    private LocalDate orderDate;
    private PaymentMethod paymentMethod;

    public Order() {
    }

    public Order(CustomerInfo customerInfo, List<OrderItem> orderItems, BigDecimal costShipping, Cost costTotal, LocalDate orderDate, PaymentMethod paymentMethod) {
        this.customerInfo = customerInfo;
        this.orderItems = orderItems;
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

    public List<OrderItem> getShoppingCart() {
        return orderItems;
    }

    public void setShoppingCart(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getCostShipping() {
        return costShipping;
    }

    public void setCostShipping(BigDecimal costShipping) {
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerInfo=" + customerInfo +
                ", orderItems=" + orderItems +
                ", costShipping=" + costShipping +
                ", costTotal=" + costTotal +
                ", orderDate=" + orderDate +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}

