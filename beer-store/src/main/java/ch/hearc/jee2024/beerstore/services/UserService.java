package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    void create(UserEntity user);
    Page<UserEntity> list(Pageable pageable);

    Optional<UserEntity> get(Long id);
    void delete(Long id);
}
