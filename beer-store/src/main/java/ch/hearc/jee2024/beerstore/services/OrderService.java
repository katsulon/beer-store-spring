package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {
    void create(OrderEntity order);
    Page<OrderEntity> list(Pageable pageable);

    Optional<OrderEntity> get(Long id);
    void delete(Long id);
}
