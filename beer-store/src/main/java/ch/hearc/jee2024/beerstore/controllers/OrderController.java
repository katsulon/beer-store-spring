package ch.hearc.jee2024.beerstore.controllers;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;
import ch.hearc.jee2024.beerstore.models.OrderBeerEntity;
import ch.hearc.jee2024.beerstore.models.OrderEntity;
import ch.hearc.jee2024.beerstore.services.BeerService;
import ch.hearc.jee2024.beerstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final BeerService beerService;

    @Autowired
    public OrderController(OrderService orderService, BeerService beerService) {
        this.orderService = orderService;
        this.beerService = beerService;
    }

    @PostMapping("/order")
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderEntity order) {
        if (order.getOrderBeers() == null || order.getOrderBeers().isEmpty()) {
            return ResponseEntity.badRequest().build(); // No beers provided
        }

        Set<OrderBeerEntity> validatedOrderBeers = new HashSet<>();

        for (OrderBeerEntity orderBeer : order.getOrderBeers()) {
            BeerEntity beer = orderBeer.getBeer();

            Optional<BeerEntity> existingBeer = beerService.get(beer.getId());
            if (existingBeer.isEmpty()) {
                return ResponseEntity.badRequest().build(); // Beer not found
            }

            orderBeer.setOrder(order);
            orderBeer.setBeer(existingBeer.get());
            validatedOrderBeers.add(orderBeer);
        }

        order.setOrderBeers(validatedOrderBeers);
        order.calculateTotalPrice();
        orderService.create(order);

        return ResponseEntity.ok(order);
    }


    @GetMapping("/order")
    public Iterable<OrderEntity> getOrders() {
        return orderService.list();
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable Long id) {
        Optional<OrderEntity> order = orderService.get(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<OrderEntity> updateOrder(@PathVariable Long id, OrderEntity order) {
        Optional<OrderEntity> existingOrder = orderService.get(id);
        if (existingOrder.isPresent()) {
            order.setId(id);
            orderService.create(order);
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Optional<OrderEntity> existingOrder = orderService.get(id);
        if (existingOrder.isPresent()) {
            orderService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
