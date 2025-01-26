package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username); // For Spring Security
}