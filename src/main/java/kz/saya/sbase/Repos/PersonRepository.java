package kz.saya.sbase.Repos;

import kz.saya.sbase.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    Optional<Person> findByUsersId(UUID id);

    Optional<Person> findByUsersLogin(String login);

    Optional<Person> findByUsersUsername(String username);

    Optional<Person> findByEmail(String email);
}