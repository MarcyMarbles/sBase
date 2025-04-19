package com.example.sbase.Controller.MVC;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginMVCController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


}
