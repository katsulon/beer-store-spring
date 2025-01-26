package ch.hearc.jee2024.beerstore.config;

import ch.hearc.jee2024.beerstore.models.UserEntity;
import ch.hearc.jee2024.beerstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Value("${spring.security.user.name}")
    private String adminUsername;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        // Check if the admin user already exists if not create one
        if (userRepository.findById(1L).isEmpty()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(adminPassword);

            UserEntity admin = new UserEntity(adminUsername, hashedPassword, UserEntity.Role.ADMIN);
            userRepository.save(admin);

            System.out.println("Admin user created:");
            System.out.println("Username: " + adminUsername);
            System.out.println("Password: " + adminPassword);
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
