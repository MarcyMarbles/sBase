package com.example.sbase.Controller.MVC;

import com.example.sbase.Entity.Interfaces.Menuable;
import com.example.sbase.Entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Menuable(
        caption = "Пользователи",
        icon = "fa-solid fa-user",
        order = 1,
        parent = "/admin"
)
@Controller
public class PersonController {

    @GetMapping("/person")
    public String personPage(
            @ModelAttribute("user") User currentUser
            ) {
        return "person";
    }
}
