package com.example.todolist.Repos;

import com.example.todolist.Entity.Category;
import com.example.todolist.Entity.Person;
import com.example.todolist.Entity.Tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    List<Tasks> findByUser(Person person);

    List<Tasks> findByUserAndCategory(Person person, Category category);

    List<Tasks> findByUserAndDeadline(Person person, Date date);

    List<Tasks> findByUserAndDeadlineBefore(Person person, Date date);

    Page<Tasks> findByUser(Person person, Pageable pageable);
}