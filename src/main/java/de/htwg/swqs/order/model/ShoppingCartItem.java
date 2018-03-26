package de.htwg.swqs.order.model;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Class will be used to convert the payload of requests to add new products
 * to a java object (with Jackson Json-processor https://github.com/FasterXML/jackson)
 */
@Embeddable
public class ShoppingCartItem {

    @Positive
    private int quantity;
    @ManyToOne(cascade= CascadeType.PERSIST)
    private Product product;

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(@Positive int quantity, @NotNull Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
