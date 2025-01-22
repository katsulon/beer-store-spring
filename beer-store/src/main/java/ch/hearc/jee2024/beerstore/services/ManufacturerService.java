package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ManufacturerService {
    void create(ManufacturerEntity manufacturer);
    Page<ManufacturerEntity> list(Pageable pageable);

    Optional<ManufacturerEntity> get(Long id);
    void delete(Long id);
}
