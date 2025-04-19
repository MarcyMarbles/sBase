package com.example.todolist.Repos;

import com.example.todolist.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByName(String name);

    Optional<Roles> findByDescription(String description);

    Optional<Roles> findByNameAndDescription(String name, String description);
}
