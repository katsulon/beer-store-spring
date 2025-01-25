package ch.hearc.jee2024.beerstore.repositories;

import ch.hearc.jee2024.beerstore.models.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> { }