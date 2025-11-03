package rohan.authService.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rohan.authService.entity.Role;
import rohan.authService.repository.RoleRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        initializeRoles();
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            log.info("Initializing default roles...");

            Role userRole = new Role();
            userRole.setName(Role.RoleType.ROLE_USER);
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName(Role.RoleType.ROLE_ADMIN);
            roleRepository.save(adminRole);

            Role moderatorRole = new Role();
            moderatorRole.setName(Role.RoleType.ROLE_MODERATOR);
            roleRepository.save(moderatorRole);

            log.info("Default roles initialized successfully!");
        } else {
            log.info("Roles already exist. Skipping initialization.");
        }
    }
}