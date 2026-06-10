package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;
import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User findByUsername(String username);

    void createUser(String username, String password, String name, Integer age, String email, Set<Long> roleIds);
    void updateUser(Long id, String username, String password, String name, Integer age, String email, Set<Long> roleIds);
    boolean isUsernameExists(String username);
    boolean isUsernameExistsForOtherUser(Long id, String username);

}