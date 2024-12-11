package ch.hearc.jee2024.beerstore.controllers;

import ch.hearc.jee2024.beerstore.models.Beer;
import ch.hearc.jee2024.beerstore.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public @ResponseBody Beer[] getBeers() {
        return beerService.list();
    }

    @GetMapping("/beer/{id}")
    public ResponseEntity<Beer> getBeer(@PathVariable Integer id) {
        Beer beer = beerService.get(id);
        if (beer != null) {
            return ResponseEntity.ok(beer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/beer/{id}")
    public ResponseEntity<Beer> updateBeer(@PathVariable Integer id, @RequestBody Beer beer) {
        Beer existingBeer = beerService.get(id);
        if (existingBeer != null) {
            beerService.create(beer);
            return ResponseEntity.ok(beer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/beer/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Integer id) {
        Beer beer = beerService.get(id);
        if (beer != null) {
            beerService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
