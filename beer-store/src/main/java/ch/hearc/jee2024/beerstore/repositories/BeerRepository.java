package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BeerRepository extends CrudRepository<BeerEntity, Long>, PagingAndSortingRepository<BeerEntity, Long> { }
