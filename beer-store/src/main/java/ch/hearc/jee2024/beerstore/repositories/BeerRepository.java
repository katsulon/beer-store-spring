package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BeerRepository extends CrudRepository<BeerEntity, Long>, PagingAndSortingRepository<BeerEntity, Long> { }
