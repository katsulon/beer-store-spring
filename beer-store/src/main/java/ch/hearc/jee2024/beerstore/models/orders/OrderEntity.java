package ch.hearc.jee2024.beerstore.models.orders;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderBeerEntity> orderBeers = new HashSet<>();

    private double totalPrice;

    public OrderEntity() { }

    public OrderEntity(Set<OrderBeerEntity> orderBeers) {
        this.orderBeers = orderBeers;
        this.calculateTotalPrice();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<OrderBeerEntity> getOrderBeers() {
        return orderBeers;
    }

    public void addBeer(BeerEntity beer, int quantity) {
        OrderBeerEntity orderBeer = new OrderBeerEntity();
        orderBeer.setOrder(this);
        orderBeer.setBeer(beer);
        orderBeer.setQuantity(quantity);
        this.orderBeers.add(orderBeer);
    }

    public void setOrderBeers(Set<OrderBeerEntity> orderBeers) {
        this.orderBeers = orderBeers;
    }

    public void removeBeer(BeerEntity beer) {
        this.orderBeers.remove(beer);
    }

    public void clearBeers() {
        this.orderBeers.clear();
    }

    public void calculateTotalPrice() {
        this.totalPrice = this.orderBeers.stream()
                .mapToDouble(orderBeer -> orderBeer.getBeer().getPrice() * orderBeer.getQuantity())
                .sum();
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
