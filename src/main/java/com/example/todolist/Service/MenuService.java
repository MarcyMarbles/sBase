package com.example.todolist.Service;

import com.example.todolist.Entity.MenuElement;
import com.example.todolist.Entity.Roles;
import com.example.todolist.Repos.MenuElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class MenuService {

    @Autowired
    private MenuElementRepository menuElementRepository;

    public Set<MenuElement> generateMenu(Set<Roles> userRoles) {
        return Set.copyOf(menuElementRepository.generateMenu(new ArrayList<>(userRoles)));
    }
}
