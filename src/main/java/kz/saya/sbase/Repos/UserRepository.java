package kz.saya.sbase.Repos;

import kz.saya.sbase.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByUsername(String username);

    List<User> findByPersonId(int personId);

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

}