package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.repositories.BeerRepository;
import ch.hearc.jee2024.beerstore.models.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Qualifier("beerService")
@Service
public class BeerServiceImplementation implements BeerService {
    @Autowired
    public BeerServiceImplementation(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void create(Beer beer) { beerRepository.save(beer); }

    @Override
    public Iterable<Beer> list() { return beerRepository.findAll(); }

    @Override
    public Optional<Beer> get(Long id) { return beerRepository.findById(id);}

    @Override
    public void delete(Long id) {
        beerRepository.deleteById(id);
    }

    private final BeerRepository beerRepository;
}
