package kz.saya.sbasecore.Entity;

import jakarta.persistence.*;
import kz.saya.sbasecore.Entity.NotPersistent.Langs;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends MappedSuperClass {
    @Column(unique = true, nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String username;

    @ManyToMany(mappedBy = "users")
    List<Person> person = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles = new HashSet<>();

    private Langs lang = Langs.RU;

    public Person getCurrentPerson() {
        if (person.isEmpty()) {
            return null;
        }
        return person
                .stream()
                .filter(e -> OffsetDateTime.now().isAfter(e.start_date_ts) && OffsetDateTime.now().isBefore(e.end_date_ts))
                .findFirst().orElse(null);
    }
}
