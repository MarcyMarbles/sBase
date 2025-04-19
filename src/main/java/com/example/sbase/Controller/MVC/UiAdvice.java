package com.example.sbase.Controller.MVC;

import com.example.sbase.Entity.MenuElement;
import com.example.sbase.Entity.Roles;
import com.example.sbase.Service.MenuService;
import com.example.sbase.Service.RolesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@ControllerAdvice
public class UiAdvice {
    private final MenuController menuController;
    private final RolesService rolesService;
    private final MenuService menuService;

    public UiAdvice(MenuController menuController, RolesService rolesService, MenuService menuService) {
        this.menuController = menuController;
        this.rolesService = rolesService;
        this.menuService = menuService;
    }

    @Controller
    static class CustomErrorController implements ErrorController {
        @RequestMapping("/error")
        public String handleError(HttpServletRequest request) {
            Object statusCode = request.getAttribute("javax.servlet.error.status_code");
            if (statusCode != null) {
                int code = Integer.parseInt(statusCode.toString());
                if (code == 404) {
                    return "redirect:/home";
                }
            }
            return "error";
        }
    }

    @ModelAttribute("menu")
    public Set<MenuElement> addMenu(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<Roles> roles = rolesService.getUserRoles(auth);
        return menuService.generateMenu(roles);
    }

    @ModelAttribute("usr")
    public UserDetails currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        return null;
    }

}
