package kz.saya.sbase.Controller.MVC;

import kz.saya.sbase.Entity.Interfaces.Menuable;
import kz.saya.sbase.Entity.Roles;
import kz.saya.sbase.Service.RolesService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Menuable(
        caption = "Roles",
        icon = "fa-solid fa-key",
        order = 2,
        parent = "/admin"
)
@Controller
@RequestMapping("/roles")
public class RolesController {

    private final RolesService rolesService;

    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping
    public String showRoles(Model model, Authentication authentication) {
        // Check if user has ADMIN role
        if (!hasAdminRole(authentication)) {
            return "redirect:/";
        }

        List<Roles> roles = rolesService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("newRole", new Roles());
        return "roles";
    }

    @PostMapping("/create")
    public String createRole(@ModelAttribute("newRole") Roles newRole, 
                            RedirectAttributes redirectAttributes,
                            Authentication authentication) {
        // Check if user has ADMIN role
        if (!hasAdminRole(authentication)) {
            return "redirect:/";
        }

        try {
            rolesService.createRole(newRole.getName(), newRole.getDescription());
            redirectAttributes.addFlashAttribute("successMessage", "Role created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating role: " + e.getMessage());
        }
        return "redirect:/roles";
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication != null && 
               authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}