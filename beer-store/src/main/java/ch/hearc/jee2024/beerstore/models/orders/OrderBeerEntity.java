package ch.hearc.jee2024.beerstore.models.orders;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "orders_beers")
public class OrderBeerEntity {
    @EmbeddedId
    private OrderBeerId id = new OrderBeerId();

    @ManyToOne
    @MapsId("orderId")
    @JsonIncludeProperties(value = {"id"})
    private OrderEntity order;

    @ManyToOne
    @MapsId("beerId")
    @JsonIncludeProperties(value = {"id"})
    private BeerEntity beer;

    private int quantity;

    public OrderBeerEntity() {}

    public OrderBeerEntity(BeerEntity beer, int quantity) {
        this.beer = beer;
        this.quantity = quantity;
    }

    // Getters and setters
    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public BeerEntity getBeer() {
        return beer;
    }

    public void setBeer(BeerEntity beer) {
        this.beer = beer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}