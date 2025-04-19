package com.example.sbase.Repos;

import com.example.sbase.Entity.MenuElement;
import com.example.sbase.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuElementRepository extends JpaRepository<MenuElement, Integer> {
    // Можно переработать в рекурсивную функцию
    @Query("select distinct e from MenuElement e join e.accessibleViaRoles r where r in :rolesList order by e.rank")
    List<MenuElement> generateMenu(@Param(value = "rolesList") List<Roles> rolesList);
}