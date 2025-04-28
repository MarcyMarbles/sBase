package kz.saya.sbaseweb.POJOs;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class UserProfileForm {
    private String username;
    private String phoneNumber;

    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String fullName;

    private MultipartFile profilePictureFile;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
    private UUID sexId;
    private String country;
}
