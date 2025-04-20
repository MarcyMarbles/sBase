package kz.saya.sbase.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Person extends MappedSuperClass {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    @ManyToOne(fetch = FetchType.LAZY)
    private Sex sex;
    private String country; // Страна

    @ManyToMany
    @JoinTable(
            name = "person_to_user",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    @OneToOne
    private FileDescriptor profilePicture;

    public User getCurrentUser() {
        if (users.isEmpty()) {
            return null;
        }
        OffsetDateTime now = OffsetDateTime.now();
        return users
                .stream()
                .filter(e -> e.getStart_date_ts().isBefore(now) && e.getEnd_date_ts().isAfter(now))
                .findFirst().orElse(null);
    }

    public String getFullName() {
        if (firstName == null) {
            firstName = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        if (middleName == null) {
            middleName = "";
        }
        return String.format("%s %s %s", firstName, lastName, middleName);
    }
}
