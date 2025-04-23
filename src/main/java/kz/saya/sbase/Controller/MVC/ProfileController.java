package kz.saya.sbase.Controller.MVC;

import kz.saya.sbase.Entity.FileDescriptor;
import kz.saya.sbase.Entity.Person;
import kz.saya.sbase.Entity.User;
import kz.saya.sbase.POJOs.UserProfileForm;
import kz.saya.sbase.Repos.SexRepository;
import kz.saya.sbase.Service.CountryService;
import kz.saya.sbase.Service.FileDescriptorService;
import kz.saya.sbase.Service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;

@Controller
public class ProfileController {

    private final UserService userService;
    private final FileDescriptorService fileDescriptorService;
    private final CountryService countryService;
    private final SexRepository sexRepository;

    public ProfileController(UserService userService, FileDescriptorService fileDescriptorService, CountryService countryService, SexRepository sexRepository) {
        this.userService = userService;
        this.fileDescriptorService = fileDescriptorService;
        this.countryService = countryService;
        this.sexRepository = sexRepository;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        User user = userService.getUserByLogin(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("countries", countryService.getAllCountries());
        model.addAttribute("sexes", userService.getAllSexes());
        return "profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute UserProfileForm form,
                                BindingResult bindingResult,
                                Model model,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {

        User user = userService.getUserByLogin(principal.getName());
        Person person = user.getCurrentPerson();

        // Update user fields
        user.setUsername(form.getUsername());

        if (person != null) {
            if (!form.getFullName().equals(person.getFullName())) {
                String[] nameParts = form.getFullName().split(" ");
                for (int i = 0; i < nameParts.length; i++) {
                    if (i == 0) {
                        person.setFirstName(nameParts[i]);
                    } else if (i == 1) {
                        person.setLastName(nameParts[i]);
                    } else {
                        person.setMiddleName(nameParts[i]);
                    }
                }
            }
            person.setEmail(form.getEmail());
            if (form.getSexId() != null) {
                person.setSex(sexRepository.findById(form.getSexId()).orElse(null));
            }
            person.setCountry(form.getCountry());
        }

        if (form.getProfilePictureFile() != null && !form.getProfilePictureFile().isEmpty()) {
            try {
                FileDescriptor file = fileDescriptorService.saveFile(form.getProfilePictureFile());
                person.setProfilePicture(file);
            } catch (Exception e) {
                model.addAttribute("profilePictureError", "Error uploading profile picture.");
                model.addAttribute("user", user);
                return "profile";
            }
        }

        if (form.getCurrentPassword() != null && !form.getCurrentPassword().isBlank()) {
            if (!userService.checkPassword(user, form.getCurrentPassword())) {
                model.addAttribute("passwordError", "Current password is incorrect.");
                model.addAttribute("user", user);
                return "profile";
            }
            if (!form.getNewPassword().equals(form.getConfirmPassword())) {
                model.addAttribute("passwordMismatchError", "New passwords do not match.");
                model.addAttribute("user", user);
                return "profile";
            }
            userService.updatePassword(user, form.getNewPassword());
        }

        userService.saveUserAndPerson(user, person);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/profile";
    }

    @GetMapping(value = "/profilePicture/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable(name = "id") UUID id) {
        // Fetch the user or person entity by ID
        User user = userService.getUserById(id);

        byte[] profilePicture = user.getCurrentPerson().getProfilePicture().getFileData();

        if (profilePicture == null || profilePicture.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(profilePicture);
    }
}
