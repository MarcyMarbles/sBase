package com.example.todolist.Controller.MVC;

import com.example.todolist.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginMVCController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


}
