package com.example.todolist.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class MenuElement extends MappedSuperClass {
    @ManyToOne
    private MenuElement parentElement;

    private String icon;
    private String url;

    @ManyToMany
    private List<Roles> accessibleViaRoles;
    // Роли которые смогут увидеть этот элемент.
    // Если элемент запрещен, то и все дочерние элементы будут запрещены
    private int rank; // порядок отображения элемента в меню
}
