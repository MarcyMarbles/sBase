package kz.saya.sbase.POJOs;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    private Integer sexId;
    private String country;
}
