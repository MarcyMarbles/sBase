package com.example.sbase.Controller.MVC;

import com.example.sbase.Entity.FileDescriptor;
import com.example.sbase.Entity.Person;
import com.example.sbase.Entity.User;
import com.example.sbase.POJOs.UserProfileForm;
import com.example.sbase.Service.FileDescriptorService;
import com.example.sbase.Service.UserService;
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

@Controller
public class ProfileController {

    private final UserService userService;
    private final FileDescriptorService fileDescriptorService;

    public ProfileController(UserService userService, FileDescriptorService fileDescriptorService) {
        this.userService = userService;
        this.fileDescriptorService = fileDescriptorService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        User user = userService.getUserByLogin(principal.getName());
        model.addAttribute("user", user);
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
            person.setFirstName(form.getFirstName());
            person.setLastName(form.getLastName());
            person.setMiddleName(form.getMiddleName());
            person.setEmail(form.getEmail());
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
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Integer id) {
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
