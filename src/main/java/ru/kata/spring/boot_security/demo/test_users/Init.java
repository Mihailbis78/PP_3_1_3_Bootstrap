package ru.kata.spring.boot_security.demo.test_users;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.*;

@Component
public class Init {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Init(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init() {
        if (roleService.findRoleByName("USER") == null) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleService.saveRole(userRole);
            System.out.println("Роль User создана");
        } else {
            System.out.println("РОЛЬ USER НЕ СОЗДАНА!");
        }

        if (roleService.findRoleByName("ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleService.saveRole(adminRole);
            System.out.println("Роль ADMIN создана");
        }
        else {
            System.out.println("РОЛЬ ADMIN НЕ СОЗДАНА!");
        }

        if (userService.getUserByEmail("user@example.com") == null) {
            User user = new User();
            user.setName("Иван");
            user.setAge(45);
            user.setEmail("user@example.com");
            user.setPassword("user");
            user.setRoles(new HashSet<>(Collections.singleton(roleService.findRoleByName("USER"))));

            userService.saveUser(user, Collections.singletonList(1L));
            System.out.println(user.getPassword());
        }

        if (userService.getUserByEmail("admin@example.com") == null) {
            User admin = new User();
            admin.setName("Сергей");
            admin.setAge(35);
            admin.setEmail("admin@example.com");
            admin.setPassword(("admin"));

            Set<Role> roles = new HashSet<>();
            roles.add(roleService.findRoleByName("ADMIN"));
            roles.add(roleService.findRoleByName("USER"));

            admin.setRoles(roles);

            List<Long> rolesIds = new ArrayList<>();
            rolesIds.add(1L);
            rolesIds.add(2L);

            userService.saveUser(admin, rolesIds);
            System.out.println(admin.getPassword());
        }
    }
}
