package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ManufacturerRepository extends CrudRepository<ManufacturerEntity, Long>, PagingAndSortingRepository<ManufacturerEntity, Long> { }