package com.example.sbase.Controller.MVC;

import com.example.sbase.Entity.Person;
import com.example.sbase.Entity.User;
import com.example.sbase.POJOs.UserMVCRegisterPOJO;
import com.example.sbase.Repos.PersonRepository;
import com.example.sbase.Repos.UserRepository;
import com.example.sbase.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String doRegister(@Valid @ModelAttribute("userForm") UserMVCRegisterPOJO dto,
                             BindingResult br,
                             RedirectAttributes ra) {

        if (userService.existsByLogin(dto.getLogin())) {
            br.rejectValue("login", "login.duplicate", "Login already exists");
        }

        if (br.hasErrors()) {
            return "register";                // остаёмся на форме, ошибки выведет Thymeleaf
        }

        userService.register(dto);            // всё что связано с Entity/Repo, прячем в сервис

        ra.addFlashAttribute("success",
                "Successfully registered! Now you can log in.");
        return "redirect:/login";
    }

}
