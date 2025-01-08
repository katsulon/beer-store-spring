package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.Beer;
import org.springframework.data.repository.CrudRepository;

public interface BeerRepository extends CrudRepository<Beer, Long> { }
