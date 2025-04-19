package com.example.todolist.Controller.MVC;

import com.example.todolist.Entity.Tasks;
import com.example.todolist.Entity.User;
import com.example.todolist.Repos.CategoryRepository;
import com.example.todolist.Repos.TasksRepository;
import com.example.todolist.Repos.UserRepository;
import com.example.todolist.Service.CategoryService;
import com.example.todolist.Service.PriorityService;
import com.example.todolist.Service.TaskService;
import com.example.todolist.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

@Controller
public class TasksController {
    private final UserRepository usersRepo;
    private final TasksRepository tasksRepo;
    private final TaskService tasksService;
    private final CategoryRepository categoryRepo;
    private final UserService userService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    public TasksController(UserRepository usersRepo, TasksRepository tasksRepo, TaskService tasksService, CategoryRepository categoryRepo, UserService userService, PriorityService priorityService, CategoryService categoryService) {
        this.usersRepo = usersRepo;
        this.tasksRepo = tasksRepo;
        this.tasksService = tasksService;
        this.categoryRepo = categoryRepo;
        this.userService = userService;
        this.priorityService = priorityService;
        this.categoryService = categoryService;
    }


    @PostMapping("/tasks/create")
    public String createTask(@ModelAttribute Tasks task, @RequestParam("dueDate") String due_Date, Principal principal) {
        User user = userService.getUserByLogin(principal.getName());
        if (user == null) {
            return "redirect:/home";
        }
        task.setDeadline(Date.valueOf(due_Date));
        tasksRepo.save(task);
        return "redirect:/tasks";
    }

    public void addToModel(Model model) {
        model.addAttribute("priorities", priorityService.getAllPriorities(curUser.getCurrentPerson()));
        model.addAttribute("categories", categoryService.getAllCategories(curUser.getCurrentPerson()));
    }


    User curUser;

    @GetMapping("/tasks")
    public String tasksUI(Model model,
                          Principal principal,
                          @RequestParam(value = "page", defaultValue = "1") int page) {
        User currentUser = userService.getUserByLogin(principal.getName());
        if (currentUser == null) {
            return "redirect:/home";
        }
        curUser = currentUser;
        int pageSize = 10;
        Page<Tasks> userTasksPage;
        userTasksPage = tasksService.getTasksByPage(page - 1, pageSize, currentUser.getCurrentPerson());


/*        userTasksPage.stream().forEach(task -> {
            task.setFormattedCreatedAt(task.getCreated_at().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            task.setFormattedDueDate(task.getDue_date().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        });*/

        model.addAttribute("userTasks", userTasksPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userTasksPage.getTotalPages());
        model.addAttribute("totalItems", userTasksPage.getTotalElements());

        return "tasks";
    }



}
