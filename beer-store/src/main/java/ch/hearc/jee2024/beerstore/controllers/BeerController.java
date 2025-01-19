package ch.hearc.jee2024.beerstore.controllers;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;
import ch.hearc.jee2024.beerstore.services.BeerService;
import ch.hearc.jee2024.beerstore.services.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class BeerController {
    private final BeerService beerService;
    private final ManufacturerService manufacturerService;

    @Autowired
    public BeerController(BeerService beerService, ManufacturerService manufacturerService) {
        this.beerService = beerService;
        this.manufacturerService = manufacturerService;
    }

    @PostMapping("/beer")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BeerEntity> createBeer(@RequestBody BeerEntity beer) {
        try {
            if (beer.getManufacturer() != null && beer.getManufacturer().getId() != null) {
                Optional<ManufacturerEntity> manufacturer = manufacturerService.get(beer.getManufacturer().getId());
                manufacturer.ifPresent(beer::setManufacturer);
            }
            beerService.create(beer);
            return ResponseEntity.ok(beer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/beer")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<BeerEntity> getBeers() {
        return beerService.list();
    }

    @GetMapping("/beer/{id}")
    public ResponseEntity<BeerEntity> getBeer(@PathVariable Long id) {
        Optional<BeerEntity> beer = beerService.get(id);
        return beer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/beer/{id}")
    public ResponseEntity<BeerEntity> updateBeer(@PathVariable Long id, @RequestBody BeerEntity beer) {
        Optional<BeerEntity> existingBeer = beerService.get(id);
        if (existingBeer.isPresent()) {
            try {
                beer.setId(id);
                beerService.create(beer);
                return ResponseEntity.ok(beer);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/beer/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Long id) {
        Optional<BeerEntity> existingBeer = beerService.get(id);
        if (existingBeer.isPresent()) {
            beerService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
