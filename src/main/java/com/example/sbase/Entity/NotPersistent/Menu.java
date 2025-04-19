package com.example.sbase.Entity.NotPersistent;

import com.example.sbase.Entity.Roles;
import com.example.sbase.XmlUtils.RoleDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Menu {
    private String title, url, icon;
    private List<Menu> subMenu;
    @JsonDeserialize(contentUsing = RoleDeserializer.class)
    private List<Roles> roles; // Roles allowed to see this menu item

    public Menu(String title, String url, String icon, List<Menu> subMenu, List<Roles> roles) {
        this.title = title;
        this.url = url;
        this.icon = icon;
        this.subMenu = subMenu;
        this.roles = roles;
    }
    public Menu(){

    }

    public List<Menu> issubMenu() {
        return subMenu;
    }

    @JsonIgnore
    public int getSize() {
        if(subMenu == null) return 0;
        return subMenu.size();
    }
    public void addRole(Roles role) {
        if(roles == null) roles = new ArrayList<>();
        roles.add(role);
    }
}
