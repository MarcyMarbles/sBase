package kz.saya.sbaseweb.Controllers.MVC;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginMVCController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


}
