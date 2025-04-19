package com.example.sbase.Controller.MVC;

import com.example.sbase.Entity.Person;
import com.example.sbase.Entity.User;
import com.example.sbase.POJOs.UserMVCRegisterPOJO;
import com.example.sbase.Repos.PersonRepository;
import com.example.sbase.Repos.UserRepository;
import com.example.sbase.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegisterMVCController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    public RegisterMVCController(UserService userService, UserRepository userRepository, PersonRepository personRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.personRepository = personRepository;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userForm", new UserMVCRegisterPOJO());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("userForm") UserMVCRegisterPOJO user,
                             Model model) {

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("fragment", "error");
            return "fragments/register-result";
        }

        if (userService.getUserByLogin(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists.");
            model.addAttribute("fragment", "error");
            return "fragments/register-result";
        }

        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            model.addAttribute("error", "Username and password cannot be empty.");
            model.addAttribute("fragment", "error");
            return "fragments/register-result";
        }

        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
            model.addAttribute("error", "First name and last name cannot be empty.");
            model.addAttribute("fragment", "error");
            return "fragments/register-result";
        }

        if (user.getEmail().isEmpty()) {
            model.addAttribute("error", "Email cannot be empty.");
            model.addAttribute("fragment", "error");
            return "fragments/register-result";
        }

        if (user.getMiddleName() == null || user.getMiddleName().isEmpty()) {
            user.setMiddleName("");
        }

        List<User> userList = new ArrayList<>();
        List<Person> personList = new ArrayList<>();

        User userToRegister = new User();
        userToRegister.setLogin(user.getUsername());
        userToRegister.setPassword(user.getPassword());
        userToRegister = userService.createUser(userToRegister);
        userList.add(userToRegister);
        Person personToRegister = new Person();
        personToRegister.setFirstName(user.getFirstName());
        personToRegister.setLastName(user.getLastName());
        personToRegister.setMiddleName(user.getMiddleName());
        personToRegister.setEmail(user.getEmail());
        personList.add(personToRegister);
        userToRegister.setPerson(personList);
        personToRegister.setUsers(userList);

        userRepository.save(userToRegister);
        personRepository.save(personToRegister);

        model.addAttribute("message", "Registered successfully as " + user.getUsername());
        model.addAttribute("fragment", "success");
        return "redirect:/login";
    }
}
