package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.orders.OrderEntity;
import ch.hearc.jee2024.beerstore.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Qualifier("orderService")
@Service
public class OrderServiceImplementation implements OrderService {
    @Autowired
    public OrderServiceImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void create(OrderEntity order) { orderRepository.save(order); }

    @Override
    public Page<OrderEntity> list(Pageable pageable) { return orderRepository.findAll(pageable); }

    @Override
    public Optional<OrderEntity> get(Long id)  { return orderRepository.findById(id); }

    @Override
    public void delete(Long id) { orderRepository.deleteById(id); }

    private final OrderRepository orderRepository;
}
