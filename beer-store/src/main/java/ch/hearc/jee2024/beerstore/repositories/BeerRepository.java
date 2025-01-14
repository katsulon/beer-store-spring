package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import org.springframework.data.repository.CrudRepository;

public interface BeerRepository extends CrudRepository<BeerEntity, Long> { }
