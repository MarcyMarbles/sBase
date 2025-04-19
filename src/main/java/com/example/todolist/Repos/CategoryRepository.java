package com.example.todolist.Repos;

import com.example.todolist.Entity.Category;
import com.example.todolist.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);

    Optional<Category> findByNameAndUserId(String name, Person userId);

    List<Category> findByUserId(Person userId);
}