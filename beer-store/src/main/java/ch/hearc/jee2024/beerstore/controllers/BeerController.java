package ch.hearc.jee2024.beerstore.controllers;

import ch.hearc.jee2024.beerstore.models.Beer;
import ch.hearc.jee2024.beerstore.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// Peut-être plutôt utiliser Optional, surtout pour les get id. Aussi list devrait plutôt retourner que certains paramètres de la bière.

@RestController
public class BeerController {
    private final BeerService beerService;

    @Autowired
    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @PostMapping(value = "/beer")
    @ResponseStatus(HttpStatus.CREATED)
    public Beer createBeer(@RequestBody Beer beer) {
        beerService.create(beer);
        return beer;
    }

    @GetMapping(value = "/beer")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Beer> getBeers() {
        return beerService.list();
    }

    @GetMapping("/beer/{id}")
    public ResponseEntity<Beer> getBeer(@PathVariable Long id) {
        Optional<Beer> beer = beerService.get(id);
        return beer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/beer/{id}")
    public ResponseEntity<Beer> updateBeer(@PathVariable Long id, @RequestBody Beer beer) {
        Optional<Beer> existingBeer = beerService.get(id);
        if (existingBeer.isPresent()) {
            beer.setId(id);
            beerService.create(beer);
            return ResponseEntity.ok(beer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/beer/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Long id) {
        Optional<Beer> existingBeer = beerService.get(id);
        if (existingBeer.isPresent()) {
            beerService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
