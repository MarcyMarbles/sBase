package com.example.todolist.Service;

import com.example.todolist.Entity.NotPersistent.Langs;
import com.example.todolist.Entity.Person;
import com.example.todolist.Entity.Priority;
import com.example.todolist.Repos.PriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityService {
    @Autowired
    private PriorityRepository priorityRepository;

    public Priority findByName(String name, Person person){
        return priorityRepository.findByNameAndUserId(name, person).orElse(null);
    }

    public Priority getOrCreateByName(String name, Person person){
        Priority priority = findByName(name, person);
        if(priority == null){
            priority = createPriority(name, person);
        }
        return priority;
    }

    public Priority createPriority(String name, Person person){
        Priority priority = new Priority();
        Langs langs = person.getCurrentUser().getLang();
        priority.setLangValue(langs, name);
        priority.setUserId(person);
        return priorityRepository.save(priority);
    }

    public List<Priority> getAllPriorities(Person person) {
        return priorityRepository.findByUserId(person);
    }
}
