package com.example.todolist.Controller.MVC;

import com.example.todolist.Entity.NotPersistent.TaskStats;
import com.example.todolist.Entity.Person;
import com.example.todolist.Entity.Tasks;
import com.example.todolist.Entity.User;
import com.example.todolist.Service.TaskService;
import com.example.todolist.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeMVCController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;


    @GetMapping("/")
    public String dashboard(Model model, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.getUserByLogin(username);
        Person person = currentUser.getCurrentPerson();

        List<Tasks> tasks = taskService.getTasksForUser(person);
        List<Tasks> todayTasks = taskService.getTasksDueToday(person);
        List<Tasks> overdueTasks = taskService.getOverdueTasks(person);

        TaskStats stats = new TaskStats(tasks);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTasks", tasks);
        model.addAttribute("todayTasks", todayTasks);
        model.addAttribute("overdueTasks", overdueTasks);
        model.addAttribute("stats", stats);
        model.addAttribute("isAdmin", currentUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN")));

        return "home";
    }


    @GetMapping("/home")
    public String homePageRedirect() {

        return "redirect:/";
    }
}
