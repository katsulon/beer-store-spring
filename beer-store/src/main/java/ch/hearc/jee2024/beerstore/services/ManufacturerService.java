package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;

import java.util.Optional;

public interface ManufacturerService {
    void create(ManufacturerEntity manufacturer);
    Iterable<ManufacturerEntity> list();
    Optional<ManufacturerEntity> get(Long id);
    void delete(Long id);
}
