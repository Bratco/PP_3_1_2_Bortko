package ru.kata.spring.boot_security.demo.controller;

import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "user-form";
    }

    @PostMapping("/add")
    public String createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String name,
                             @RequestParam(required = false) Integer age,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) Set<Long> roleIds) {

        userService.createUser(username, password, name, age, email, roleIds);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "user-form";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @RequestParam String username,
                             @RequestParam(required = false) String password,
                             @RequestParam String name,
                             @RequestParam(required = false) Integer age,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) Set<Long> roleIds) {

        userService.updateUser(id, username, password, name, age, email, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}