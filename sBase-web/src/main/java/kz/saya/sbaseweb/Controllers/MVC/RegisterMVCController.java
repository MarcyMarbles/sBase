package kz.saya.sbaseweb.Controllers.MVC;

import jakarta.validation.Valid;
import kz.saya.sbasecore.POJOs.UserMVCRegisterPOJO;
import kz.saya.sbasecore.Repositories.PersonRepository;
import kz.saya.sbasecore.Repositories.UserRepository;
import kz.saya.sbasecore.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
