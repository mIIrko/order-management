package de.htwg.swqs.order.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingCart {

    /**
     * Counter for the shopping cart {@code id}, used when creating new shopping carts
     */
    private static long idGenerator= 0L;

    @Id
    private long id;
    @ElementCollection
    private List<ShoppingCartItem> itemsInShoppingCart;
    private BigDecimal cartTotalSum;

    public ShoppingCart() {
        this.itemsInShoppingCart = new ArrayList<ShoppingCartItem>();
        this.cartTotalSum = new BigDecimal(0);
        // set a new id to the created shopping cart
        this.id = ++idGenerator;
    }

    public ShoppingCart(long id, List<ShoppingCartItem> itemsInShoppingCart, BigDecimal cartTotalSum) {
        this.id = id;
        this.itemsInShoppingCart = itemsInShoppingCart;
        this.cartTotalSum = cartTotalSum;
    }

    public long getId() {
        return id;
    }

    public List<ShoppingCartItem> getItemsInShoppingCart() {
        return itemsInShoppingCart;
    }

    public void setItemsInShoppingCartAsList(List<ShoppingCartItem> itemsInShoppingCart) {
        this.itemsInShoppingCart = itemsInShoppingCart;
    }

    public BigDecimal getCartTotalSum() {
        return cartTotalSum;
    }

    public void setCartTotalSum(BigDecimal cartTotalSum) {
        this.cartTotalSum = cartTotalSum;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", itemsInShoppingCart=" + itemsInShoppingCart +
                ", cartTotalSum=" + cartTotalSum +
                '}';
    }
}
