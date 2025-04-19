package com.example.todolist.Service;

import com.example.todolist.Entity.Person;
import com.example.todolist.Entity.Roles;
import com.example.todolist.Entity.User;
import com.example.todolist.POJOs.UserAuthPOJO;
import com.example.todolist.Repos.UserRepository;
import com.example.todolist.Security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;
    private final RolesService rolesService;
    private final PersonService personService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RolesService rolesService, PersonService personService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.rolesService = rolesService;
        this.personService = personService;
    }

    public User createUser(User user) {
        if (user.getLogin() == null || user.getPassword() == null) {
            return null;
        }
        Roles roles = rolesService.getRoleByName("ROLE_USER");
        if (roles == null) {
            return null;
        }
        user.getRoles().add(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getLogin());
        return userRepository.save(user);
    }

    public List<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    public User login(String login, String password) {
        User user = userRepository.findByLogin(login).orElse(null);
        if (user == null) {
            return null;
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean isUserAlreadyCreated(UserAuthPOJO userAuthPOJO) {
        return userRepository
                       .findByLogin(userAuthPOJO.login())
                       .orElse(null) != null;
    }

    public User extractUserFromToken(String token) {
        token = token.substring(7);
        String login = jwtUtils.extractLogin(token);
        if (login == null) {
            return null;
        }
        return getUserByLogin(login);
    }

    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public void saveUserAndPerson(User user, Person person) {
        // TODO: Нарушает принцип единственной ответственности
        userRepository.save(user);
        personService.savePerson(person);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

}
