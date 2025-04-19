package com.example.sbase.Service;

import com.example.sbase.Entity.MenuElement;
import com.example.sbase.Entity.Roles;
import com.example.sbase.Repos.MenuElementRepository;
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
