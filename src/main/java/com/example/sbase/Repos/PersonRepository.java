package com.example.sbase.Repos;

import com.example.sbase.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsersId(Integer id);

    Optional<Person> findByUsersLogin(String login);

    Optional<Person> findByUsersUsername(String username);

    Optional<Person> findByEmail(String email);
}