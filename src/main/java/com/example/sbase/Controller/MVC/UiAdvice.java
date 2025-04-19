package com.example.sbase.Controller.MVC;

import com.example.sbase.Entity.MenuElement;
import com.example.sbase.Entity.Roles;
import com.example.sbase.Mappers.MenuMapper;
import com.example.sbase.POJOs.MenuPOJO;
import com.example.sbase.Service.MenuService;
import com.example.sbase.Service.RolesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
@ControllerAdvice
public class UiAdvice {
    private final RolesService rolesService;
    private final MenuService menuService;
    private final MenuMapper menuMapper;

    public UiAdvice(RolesService rolesService, MenuService menuService, MenuMapper menuMapper) {
        this.rolesService = rolesService;
        this.menuService = menuService;
        this.menuMapper = menuMapper;
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
    public List<MenuPOJO> addMenu() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<Roles> roles = rolesService.getUserRoles(auth);
        LinkedHashSet<MenuElement> flat = menuService.generateMenu(roles);
        return menuMapper.toDtoTree(flat);
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
