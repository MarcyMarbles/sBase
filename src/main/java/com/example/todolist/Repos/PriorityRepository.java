package com.example.todolist.Repos;

import com.example.todolist.Entity.Person;
import com.example.todolist.Entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    Optional<Priority> findByNameAndUserId(String name, Person userId);
    List<Priority> findByUserId(Person userId);
}