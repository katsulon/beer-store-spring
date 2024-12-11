package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.Beer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Qualifier("beerRepository")
@Repository
public class BeerRepositoryImplementation implements BeerRepository {
    private final Map<Integer, Beer> beers = new HashMap<>();

    @Override
    public void create(Beer beer) {
        beers.put(beer.id(), beer);
    }

    @Override
    public Beer[] list() {
        return beers.values().toArray(new Beer[0]);
    }

    @Override
    public Beer get(Integer id) {
        return beers.get(id);
    }

    @Override
    public void delete(Integer id) {
        beers.remove(id);
    }
}
