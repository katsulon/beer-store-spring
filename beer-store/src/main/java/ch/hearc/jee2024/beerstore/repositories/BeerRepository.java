package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.Beer;

public interface BeerRepository {
    void create(Beer beer);
    Beer[] list();
    Beer get(Integer id);
    void delete(Integer id);
}
