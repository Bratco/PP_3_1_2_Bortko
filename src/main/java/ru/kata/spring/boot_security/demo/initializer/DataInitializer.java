package ru.kata.spring.boot_security.demo.initializer;

import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public DataInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== DataInitializer START ===");

        try {
            Role adminRole = roleService.findByName("ROLE_ADMIN");
            Role userRole = roleService.findByName("ROLE_USER");

            if (adminRole == null) {
                adminRole = new Role("ROLE_ADMIN");
                roleService.save(adminRole);
                System.out.println("Created ROLE_ADMIN");
            }

            if (userRole == null) {
                userRole = new Role("ROLE_USER");
                roleService.save(userRole);
                System.out.println("Created ROLE_USER");
            }

            if (userService.findByUsername("admin") == null) {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                roles.add(userRole);

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin");
                admin.setName("Admin User");
                admin.setAge(30);
                admin.setEmail("admin@example.com");
                admin.setRoles(roles);

                userService.saveUser(admin);
                System.out.println("Created admin user");
            }

            if (userService.findByUsername("user") == null) {
                Set<Role> roles = new HashSet<>();
                roles.add(userRole);

                User user = new User();
                user.setUsername("user");
                user.setPassword("user");
                user.setName("Max");
                user.setAge(25);
                user.setEmail("user@example.com");
                user.setRoles(roles);

                userService.saveUser(user);
                System.out.println("Created regular user");
            }

            System.out.println("=== DataInitializer FINISHED ===");

        } catch (Exception e) {
            System.err.println("Error in DataInitializer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}