package kz.saya.sbase.Service;

import kz.saya.sbase.Entity.Person;
import kz.saya.sbase.Entity.Roles;
import kz.saya.sbase.Entity.Sex;
import kz.saya.sbase.Entity.User;
import kz.saya.sbase.POJOs.UserAuthPOJO;
import kz.saya.sbase.POJOs.UserMVCRegisterPOJO;
import kz.saya.sbase.Repos.SexRepository;
import kz.saya.sbase.Repos.UserRepository;
import kz.saya.sbase.Security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;
    private final RolesService rolesService;
    private final PersonService personService;
    private final SexRepository sexRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RolesService rolesService, PersonService personService, SexRepository sexRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.rolesService = rolesService;
        this.personService = personService;
        this.sexRepository = sexRepository;
    }

    public User createUser(User user) {
        if (user.getLogin() == null || user.getPassword() == null) {
            return null;
        }
        Roles roles = rolesService.getDefaultRole();
        if (roles == null) {
            return null;
        }
        user.getRoles().add(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getLogin());
        return userRepository.save(user);
    }

    public User createUserFromExternal(User user) {
        if (user.getPassword() != null) {
            throw new IllegalStateException("Пароль при передаче из внешнего источника не должен быть установлен");
            // AuthService должен сам установить пароль
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Roles defaultRole = rolesService.getDefaultRole();
            if (defaultRole != null) {
                user.getRoles().add(defaultRole);
            }
        }
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

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public void register(UserMVCRegisterPOJO dto) {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getLogin());
        user.setRoles(Set.of(rolesService.getDefaultRole()));
        user = userRepository.save(user);
        userList.add(user);

        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setMiddleName(dto.getMiddleName());
        person.setEmail(dto.getEmail());
        person.setUsers(userList);
        personService.savePerson(person);
    }

    public List<Sex> getAllSexes() {
        return sexRepository.findAll();
    }

}
