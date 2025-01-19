package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Long>, PagingAndSortingRepository<OrderEntity, Long> { }