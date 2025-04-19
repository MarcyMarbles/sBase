package com.example.todolist.Controller.Rest;

import com.example.todolist.Entity.Tasks;
import com.example.todolist.Entity.User;
import com.example.todolist.Service.AuthService;
import com.example.todolist.Service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/protected/tasks")
public class TaskController {

    private final AuthService authService;
    private final TaskService taskService;

    public TaskController(AuthService authService, TaskService taskService) {
        this.authService = authService;
        this.taskService = taskService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskRequestPOJO task, HttpServletRequest request) {
        if (!task.isValid()) {
            return ResponseEntity.badRequest().body("Task isn't valid");
        }
        User user = authService.getAuthenticatedUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }else if(user.getCurrentPerson() == null){
            return ResponseEntity.status(401).body("Continue registration please on /api/protected/persons/create");
        }
        Tasks createdTask = taskService.createTask(task, user.getCurrentPerson());
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getTasks(HttpServletRequest request) {
        User user = authService.getAuthenticatedUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(taskService.getTasksByPerson(user.getCurrentPerson()));
    }

    @GetMapping("/get/{category}")
    public ResponseEntity<?> getTasksByCategory(@PathVariable String category, HttpServletRequest request) {
        User user = authService.getAuthenticatedUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(taskService.getTasksByPersonAndCategory(user.getCurrentPerson(), category));
    }

    @PostMapping("/close/{taskId}")
    public ResponseEntity<?> closeTaskById(@PathVariable Integer taskId, HttpServletRequest request) {
        User user = authService.getAuthenticatedUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        Tasks task = taskService.closeTaskById(taskId);
        if (task == null) {
            return ResponseEntity.status(404).body("Task not found");
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping("/delete/{taskId}")
    public ResponseEntity<?> deleteTaskById(@PathVariable Integer taskId, HttpServletRequest request) {
        User user = authService.getAuthenticatedUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        Tasks task = taskService.deleteTaskById(taskId);
        if (task == null) {
            return ResponseEntity.status(404).body("Task not found");
        }
        return ResponseEntity.ok(task);
    }

    @Getter
    @Setter
    public static class TaskRequestPOJO {
        private String name;
        private String category;
        private String priority;
        private Date deadline; // Nullable
        private String description; // Nullable

        public boolean isValid() {
            return name != null && category != null && priority != null;
        }
    }
}

