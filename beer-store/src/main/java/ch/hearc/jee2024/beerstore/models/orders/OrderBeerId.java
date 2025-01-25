package ch.hearc.jee2024.beerstore.models.orders;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderBeerId implements Serializable {
    private Long orderId;
    private Long beerId;

    public OrderBeerId() {}

    public OrderBeerId(Long orderId, Long beerId) {
        this.orderId = orderId;
        this.beerId = beerId;
    }

    // Getters, setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getBeerId() {
        return beerId;
    }

    public void setBeerId(Long beerId) {
        this.beerId = beerId;
    }

    @Override
    public boolean equals(Object id2) {
        if (this == id2) return true;
        if (id2 == null || getClass() != id2.getClass()) return false;
        OrderBeerId that = (OrderBeerId) id2;
        return orderId.equals(that.orderId) && beerId.equals(that.beerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, beerId);
    }

}

