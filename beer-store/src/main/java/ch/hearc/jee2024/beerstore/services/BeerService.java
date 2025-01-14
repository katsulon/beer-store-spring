package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.BeerEntity;

import java.util.Optional;

public interface BeerService {
    void create(BeerEntity beer);
    Iterable<BeerEntity> list();
    Optional<BeerEntity> get(Long id);
    void delete(Long id);
}
