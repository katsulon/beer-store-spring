package ch.hearc.jee2024.beerstore.controllers;

import ch.hearc.jee2024.beerstore.models.UserEntity;
import ch.hearc.jee2024.beerstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        UserEntity user = userService.get(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if(!user.getUsername().equals(currentUser.getUsername()) && !currentUser.getUsername().equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity createUser(@RequestBody UserEntity user) {
        userService.create(user);
        return user;
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity user, @AuthenticationPrincipal UserDetails currentUser) {
        UserEntity existingUser = userService.get(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        if(!existingUser.getUsername().equals(currentUser.getUsername()) && !currentUser.getUsername().equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        user.setId(id);
        userService.create(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        UserEntity user = userService.get(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if(!user.getUsername().equals(currentUser.getUsername()) && !currentUser.getUsername().equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
