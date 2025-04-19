package com.example.sbase.Entity;

import jakarta.persistence.Column;
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
    private String name; // название элемента меню
    private String code; // код элемента меню (для поиска в БД)
    private String parentCode; // код родительского элемента меню (для поиска в БД)
    private boolean groupp = false; // true если это папка
    // p в конце чтобы Postgres не считал это ключевым словом

    @ManyToMany
    private List<Roles> accessibleViaRoles;
    // Роли которые смогут увидеть этот элемент.
    // Если элемент запрещен, то и все дочерние элементы будут запрещены
    private int rank; // порядок отображения элемента в меню
}
