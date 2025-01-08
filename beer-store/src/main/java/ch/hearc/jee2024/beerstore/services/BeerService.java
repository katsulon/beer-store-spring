package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.Beer;

import java.util.Optional;

public interface BeerService {
    void create(Beer beer);
    Iterable<Beer> list();
    Optional<Beer> get(Long id);
    void delete(Long id);
}
