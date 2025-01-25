package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.UserEntity;
import ch.hearc.jee2024.beerstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Qualifier("userService")
@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    public UserServiceImplementation(UserRepository userRepository) { this.userRepository = userRepository;}

    @Override
    public void create(UserEntity user) {
        /*Map encoders = new HashMap<>();
        encoders.put("sha256", new StandardPasswordEncoder());*/
        userRepository.save(user);
    }

    @Override
    public Page<UserEntity> list(Pageable pageable) { return userRepository.findAll(pageable); }

    @Override
    public Optional<UserEntity> get(Long id) { return userRepository.findById(id); }

    @Override
    public void delete(Long id) { userRepository.deleteById(id); }

    private final UserRepository userRepository;
}
