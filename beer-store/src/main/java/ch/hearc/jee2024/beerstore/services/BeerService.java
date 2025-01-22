package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BeerService {
    void create(BeerEntity beer);
    Page<BeerEntity> list(Pageable pageable);
    Optional<BeerEntity> get(Long id);
    void delete(Long id);
}
