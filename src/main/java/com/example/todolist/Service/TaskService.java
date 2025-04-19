package com.example.todolist.Service;

import com.example.todolist.Controller.Rest.TaskController;
import com.example.todolist.Entity.*;
import com.example.todolist.Entity.NotPersistent.Langs;
import com.example.todolist.Repos.TasksRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    private final TasksRepository tasksRepository;
    private final CategoryService categoryService;
    private final PriorityService priorityService;

    public TaskService(TasksRepository tasksRepository, CategoryService categoryService, PriorityService priorityService) {
        this.tasksRepository = tasksRepository;
        this.categoryService = categoryService;
        this.priorityService = priorityService;
    }


    private boolean isTaskValid(String name, String category, String priority, Person person) {
        return name != null && category != null && priority != null && person != null;
    }

    public Tasks createTask(String name, String category, String priority, String description, Date deadline, Person person) {
        if (!isTaskValid(name, category, priority, person)) {
            throw new RuntimeException("Task is not valid to be created");
        }
        Category taskCategory = categoryService.getOrCreateByName(category, person);
        Priority taskPriority = priorityService.getOrCreateByName(priority, person);
        Langs lang = person.getCurrentUser().getLang();
        Tasks tasks = new Tasks();
        tasks.setUser(person);
        tasks.setPriority(taskPriority);
        tasks.setCategory(taskCategory);
        tasks.setLangValue(lang, name);
        tasks.setDescription(lang, description);
        tasks.setDeadline(deadline);
        return tasksRepository.save(tasks);
    }

    public List<Tasks> getTasksByPerson(Person person) {
        return tasksRepository.findByUser(person);
    }

    public List<Tasks> getTasksByPersonAndCategory(Person person, Category category) {
        return tasksRepository.findByUserAndCategory(person, category);
    }

    public List<Tasks> getTasksByPersonAndCategory(Person person, String category) {
        Category taskCategory = categoryService.getOrCreateByName(category, person);
        return tasksRepository.findByUserAndCategory(person, taskCategory);
    }

    public Tasks createTask(TaskController.TaskRequestPOJO taskRequestPOJO, Person person) {
        return createTask(taskRequestPOJO.getName(), taskRequestPOJO.getCategory(), taskRequestPOJO.getPriority(),
                taskRequestPOJO.getDescription(), taskRequestPOJO.getDeadline(), person);
    }

    public List<Tasks> getTasksForUser(Person person) {
        return tasksRepository.findByUser(person);
    }

    public List<Tasks> getTasksDueToday(Person person) {
        return tasksRepository.findByUserAndDeadline(person, new Date());
    }

    public List<Tasks> getOverdueTasks(Person person) {
        return tasksRepository.findByUserAndDeadlineBefore(person, new Date());
    }

    public Page<Tasks> getTasksByPage(int page, int size, Person person) {
        return tasksRepository.findByUser(person, PageRequest.of(page, size));
    }

    private Tasks getOrThrowTaskById(Integer taskId) {
        return tasksRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Tasks closeTaskById(Integer taskId) {
        Tasks task = getOrThrowTaskById(taskId);
        task.setEnd_date_ts(OffsetDateTime.now());
        return tasksRepository.save(task);
    }

    public Tasks deleteTaskById(Integer taskId) {
        Tasks task = getOrThrowTaskById(taskId);
        task.setDeleted_ts(OffsetDateTime.now());
        return tasksRepository.save(task);
    }

}
