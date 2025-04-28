package kz.saya.sbasecore.Service;

import kz.saya.sbasecore.Entity.Person;
import kz.saya.sbasecore.Entity.Roles;
import kz.saya.sbasecore.Entity.Sex;
import kz.saya.sbasecore.Entity.User;
import kz.saya.sbasecore.POJOs.UserMVCRegisterPOJO;
import kz.saya.sbasecore.Repositories.SexRepository;
import kz.saya.sbasecore.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RolesService rolesService;
    private final PersonService personService;
    private final SexRepository sexRepository;

    public UserService(UserRepository userRepository,
                       RolesService rolesService,
                       PersonService personService,
                       SexRepository sexRepository) {
        this.userRepository = userRepository;
        this.rolesService = rolesService;
        this.personService = personService;
        this.sexRepository = sexRepository;
    }

    public User createUser(User user) {
        if (user.getLogin() == null || user.getPassword() == null) {
            return null;
        }
        Roles defaultRole = rolesService.getDefaultRole();
        if (defaultRole == null) {
            return null;
        }
        user.getRoles().add(defaultRole);
        user.setUsername(user.getLogin());
        return userRepository.save(user);
    }

    public User createUserFromExternal(User user) {
        if (user.getPassword() != null) {
            throw new IllegalStateException("Password must not be set when creating user from external source.");
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

    public boolean isUserAlreadyCreated(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public void saveUserAndPerson(User user, Person person) {
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
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setUsername(dto.getLogin());
        Roles defaultRole = rolesService.getDefaultRole();
        if (defaultRole != null) {
            user.setRoles(Set.of(defaultRole));
        }
        user = userRepository.save(user);

        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setMiddleName(dto.getMiddleName());
        person.setEmail(dto.getEmail());
        person.setUsers(List.of(user));
        personService.savePerson(person);
    }

    public List<Sex> getAllSexes() {
        return sexRepository.findAll();
    }
}
