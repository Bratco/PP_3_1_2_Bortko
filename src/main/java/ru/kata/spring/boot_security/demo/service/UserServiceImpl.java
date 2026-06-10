package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found with id: " + id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


    @Transactional
    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(User user) {

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {

            User existingUser = getUserById(user.getId());
            user.setPassword(existingUser.getPassword());
        }
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    @Transactional
    @Override
    public void createUser(String username, String password, String name,
                           Integer age, String email, Set<Long> roleIds) {

        if (isUsernameExists(username)) {
            throw new RuntimeException("User with username '" + username + "' already exists");
        }


        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);


        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = roleService.findByIds(roleIds);
            user.setRoles(roles);
        }

        saveUser(user);
    }
    @Transactional
    @Override
    public void updateUser(Long id, String username, String password,
                           String name, Integer age, String email, Set<Long> roleIds) {

        User existingUser = getUserById(id);


        if (isUsernameExistsForOtherUser(id, username)) {
            throw new RuntimeException("User with username '" + username + "' already exists");
        }


        existingUser.setUsername(username);
        if (password != null && !password.isEmpty()) {
            existingUser.setPassword(password);
        }
        existingUser.setName(name);
        existingUser.setAge(age);
        existingUser.setEmail(email);


        if (roleIds != null) {
            Set<Role> roles = roleService.findByIds(roleIds);
            existingUser.setRoles(roles);
        }

        updateUser(existingUser);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean isUsernameExistsForOtherUser(Long id, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null && !user.getId().equals(id);
    }
}