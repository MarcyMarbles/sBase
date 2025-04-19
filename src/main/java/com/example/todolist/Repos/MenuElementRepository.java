package com.example.todolist.Repos;

import com.example.todolist.Entity.MenuElement;
import com.example.todolist.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuElementRepository extends JpaRepository<MenuElement, Integer> {
    // Можно переработать в рекурсивную функцию
    @Query("select distinct e from MenuElement e join e.accessibleViaRoles r where r in :rolesList order by e.rank")
    List<MenuElement> generateMenu(List<Roles> rolesList);
}