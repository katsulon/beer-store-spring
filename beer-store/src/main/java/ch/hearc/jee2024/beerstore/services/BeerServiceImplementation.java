package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.repositories.BeerRepository;
import ch.hearc.jee2024.beerstore.models.BeerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void create(BeerEntity beer) {
        if (beer.getStock() >= 0) {
            beerRepository.save(beer);
        } else {
            String message = "Stock cannot be negative";
            System.out.println(message);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public Page<BeerEntity> list(Pageable pageable) { return beerRepository.findAll(pageable); }

    @Override
    public Optional<BeerEntity> get(Long id) { return beerRepository.findById(id);}

    @Override
    public void delete(Long id) {
        beerRepository.deleteById(id);
    }

    private final BeerRepository beerRepository;
}
