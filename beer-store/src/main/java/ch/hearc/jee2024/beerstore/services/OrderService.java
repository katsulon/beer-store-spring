package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.OrderEntity;

import java.util.Optional;

public interface OrderService {
    void create(OrderEntity order);
    Iterable<OrderEntity> list();
    Optional<OrderEntity> get(Long id);
    void delete(Long id);
}
