package ch.hearc.jee2024.beerstore.controllers;

import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;
import ch.hearc.jee2024.beerstore.models.UserEntity;
import ch.hearc.jee2024.beerstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<UserEntity> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        int defaultPageOne = page - 1;
        if (defaultPageOne < 0) {
            defaultPageOne = 0;
        }
        Pageable pageable = PageRequest.of(defaultPageOne, size);
        Page<UserEntity> userPage = userService.list(pageable);
        return userPage.getContent();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id) {
        return userService.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        userService.create(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        return userService.get(id)
                .map(existingUser -> {
                    user.setId(id);
                    userService.create(user);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
