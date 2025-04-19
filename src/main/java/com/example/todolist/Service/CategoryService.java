package com.example.todolist.Service;

import com.example.todolist.Entity.Category;
import com.example.todolist.Entity.NotPersistent.Langs;
import com.example.todolist.Entity.Person;
import com.example.todolist.Repos.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findByName(String name, Person user) {
        return categoryRepository.findByNameAndUserId(name, user).orElse(null);
    }

    public Category getOrCreateByName(String name, Person person) {
        Category category = findByName(name, person);
        if (category == null) {
            category = createCategory(name, person);
        }
        return category;
    }

    public Category createCategory(String name, Person user) {
        Category category = new Category();
        Langs langs = user.getCurrentUser().getLang();
        category.setLangValue(langs, name);
        category.setUserId(user);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(Person person) {
        return categoryRepository.findByUserId(person);
    }


}
