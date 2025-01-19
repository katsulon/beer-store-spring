package ch.hearc.jee2024.beerstore.controllers;

import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;
import ch.hearc.jee2024.beerstore.services.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PostMapping(value = "/manufacturer")
    @ResponseStatus(HttpStatus.CREATED)
    public ManufacturerEntity createManufacturer(@RequestBody ManufacturerEntity manufacturer) {
        manufacturerService.create(manufacturer);
        return manufacturer;
    }

    @GetMapping(value = "/manufacturer")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<ManufacturerEntity> getManufacturers() {
        return manufacturerService.list();
    }

    @GetMapping("/manufacturer/{id}")
    public ResponseEntity<ManufacturerEntity> getManufacturer(@PathVariable Long id) {
        Optional<ManufacturerEntity> manufacturer = manufacturerService.get(id);
        return manufacturer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/manufacturer/{id}")
    public ResponseEntity<ManufacturerEntity> updateManufacturer(@PathVariable Long id, @RequestBody ManufacturerEntity manufacturer) {
        Optional<ManufacturerEntity> existingManufacturer = manufacturerService.get(id);
        if (existingManufacturer.isPresent()) {
            manufacturer.setId(id);
            manufacturerService.create(manufacturer);
            return ResponseEntity.ok(manufacturer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/manufacturer/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Long id) {
        Optional<ManufacturerEntity> existingManufacturer = manufacturerService.get(id);
        if (existingManufacturer.isPresent()) {
            manufacturerService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
