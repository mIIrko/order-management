package de.htwg.swqs.order.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    @Id
    private long id;
    @Transient
    private String name;
    @Transient
    private String description;
    private BigDecimal priceEuro;

    public Product() {
    }

    public Product(long id, String name, String description, BigDecimal priceEuro) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceEuro = priceEuro;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPriceEuro() {
        return priceEuro;
    }

    public void setPriceEuro(BigDecimal priceEuro) {
        this.priceEuro = priceEuro;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priceEuro=" + priceEuro +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(priceEuro, product.priceEuro);
    }
}
