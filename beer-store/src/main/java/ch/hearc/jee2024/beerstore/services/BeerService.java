package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.Beer;

public interface BeerService {
    void create(Beer beer);
    Beer[] list();
    Beer get(Integer id);
    void delete(Integer id);
}
