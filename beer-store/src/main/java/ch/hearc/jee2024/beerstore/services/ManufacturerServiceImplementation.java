package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;
import ch.hearc.jee2024.beerstore.repositories.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Qualifier("manufacturerService")
@Service
public class ManufacturerServiceImplementation implements ManufacturerService {
    @Autowired
    public ManufacturerServiceImplementation(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public void create(ManufacturerEntity manufacturer) { manufacturerRepository.save(manufacturer); }

    @Override
    public Iterable<ManufacturerEntity> list() { return manufacturerRepository.findAll(); }

    @Override
    public Optional<ManufacturerEntity> get(Long id) { return manufacturerRepository.findById(id);}

    @Override
    public void delete(Long id) {
        manufacturerRepository.deleteById(id);
    }

    private final ManufacturerRepository manufacturerRepository;
}

