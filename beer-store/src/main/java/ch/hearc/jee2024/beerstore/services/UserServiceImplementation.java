package ch.hearc.jee2024.beerstore.services;

import ch.hearc.jee2024.beerstore.models.UserEntity;
import ch.hearc.jee2024.beerstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Qualifier("userService")
@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void create(UserEntity user) {
        String password = user.getPassword();
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    @Override
    public Page<UserEntity> list(Pageable pageable) { return userRepository.findAll(pageable); }

    @Override
    public Optional<UserEntity> get(Long id) { return userRepository.findById(id); }

    @Override
    public Optional<UserEntity> findByUsername(String username) { return userRepository.findByUsername(username);}

    @Override
    public void delete(Long id) { userRepository.deleteById(id); }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
}
