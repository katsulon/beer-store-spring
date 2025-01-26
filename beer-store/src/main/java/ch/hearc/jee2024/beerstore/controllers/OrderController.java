package ch.hearc.jee2024.beerstore.controllers;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import ch.hearc.jee2024.beerstore.models.UserEntity;
import ch.hearc.jee2024.beerstore.models.orders.OrderBeerEntity;
import ch.hearc.jee2024.beerstore.models.orders.OrderEntity;
import ch.hearc.jee2024.beerstore.services.BeerService;
import ch.hearc.jee2024.beerstore.services.OrderService;
import ch.hearc.jee2024.beerstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final BeerService beerService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, BeerService beerService , UserService userService) {
        this.orderService = orderService;
        this.beerService = beerService;
        this.userService = userService;
    }

    @PostMapping("/order")
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderEntity order, @AuthenticationPrincipal UserDetails currentUser) {
        if (order.getOrderBeers() == null || order.getOrderBeers().isEmpty()) {
            return ResponseEntity.badRequest().build(); // No beers provided
        }

        UserEntity user = new UserEntity(currentUser.getUsername(), currentUser.getPassword(), UserEntity.Role.USER);
        userService.findByUsername(user.getUsername()).ifPresentOrElse(
                order::setUser,
                () -> order.setUser(user)
        );

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
    public @ResponseBody List<OrderEntity> getOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        int defaultPageOne = page - 1;
        if (defaultPageOne < 0) {
            defaultPageOne = 0;
        }
        Pageable pageable = PageRequest.of(defaultPageOne, size);
        Page<OrderEntity> orderPage = orderService.list(pageable);
        return orderPage.getContent();
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        Optional<OrderEntity> order = orderService.get(id);
        if (order.isPresent()
                && order.get().getUser().getUsername().equals(currentUser.getUsername())
                || currentUser.getUsername().equals("admin")) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<OrderEntity> updateOrder(@PathVariable Long id, @RequestBody OrderEntity order, @AuthenticationPrincipal UserDetails currentUser) {
        Optional<OrderEntity> existingOrder = orderService.get(id);

        if (existingOrder.isPresent()
                && existingOrder.get().getUser().getUsername().equals(currentUser.getUsername())
                || currentUser.getUsername().equals("admin")) {
            order.setId(id);
            order.setUser(existingOrder.get().getUser());

            Set<OrderBeerEntity> validatedOrderBeers = new HashSet<>();

            for (OrderBeerEntity orderBeer : order.getOrderBeers()) {
                BeerEntity beer = orderBeer.getBeer();

                // Check if beer already exists in the order
                Optional<OrderBeerEntity> existingOrderBeer = existingOrder.get().getOrderBeers().stream()
                        .filter(ob -> ob.getBeer().getId().equals(beer.getId()))
                        .findFirst();

                if (existingOrderBeer.isPresent()) {
                    // Update the quantity if the beer already exists
                    existingOrderBeer.get().setQuantity(orderBeer.getQuantity());
                    validatedOrderBeers.add(existingOrderBeer.get());
                } else {
                    Optional<BeerEntity> existingBeer = beerService.get(beer.getId());
                    if (existingBeer.isEmpty()) {
                        return ResponseEntity.badRequest().build(); // Beer not found
                    }

                    orderBeer.setOrder(order);
                    orderBeer.setBeer(existingBeer.get());
                    validatedOrderBeers.add(orderBeer);
                }
            }

            order.setOrderBeers(validatedOrderBeers);
            order.calculateTotalPrice();
            orderService.create(order);

            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/order/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        Optional<OrderEntity> existingOrder = orderService.get(id);
        if (existingOrder.isPresent()
                && existingOrder.get().getUser().getUsername().equals(currentUser.getUsername())
                || currentUser.getUsername().equals("admin")) {
            orderService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
