package kz.saya.sbasecore.Repositories;

import kz.saya.sbasecore.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByUsername(String username);

    List<User> findByPersonId(UUID personId);

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

}
