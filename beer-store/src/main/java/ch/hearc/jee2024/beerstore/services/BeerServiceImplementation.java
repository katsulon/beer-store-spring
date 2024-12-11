package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.repositories.BeerRepository;
import ch.hearc.jee2024.beerstore.models.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Qualifier("beerService")
@Service
public class BeerServiceImplementation implements BeerService {
    @Autowired
    public BeerServiceImplementation(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void create(Beer beer) {
        beerRepository.create(beer);
    }

    @Override
    public Beer[] list() {
        return beerRepository.list();
    }

    @Override
    public Beer get(Integer id) {
        return beerRepository.get(id);
    }

    @Override
    public void delete(Integer id) {
        beerRepository.delete(id);
    }

    private final BeerRepository beerRepository;
}
