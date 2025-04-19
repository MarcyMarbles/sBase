package com.example.todolist.Controller.Rest;

import com.example.todolist.Entity.Person;
import com.example.todolist.Entity.User;
import com.example.todolist.POJOs.PersonPOJO;
import com.example.todolist.Security.JwtUtils;
import com.example.todolist.Service.PersonService;
import com.example.todolist.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/protected/persons")
public class PersonController {

    private final PersonService personService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public PersonController(PersonService personService, JwtUtils jwtUtils, UserService userService) {
        this.personService = personService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<PersonResponse> createPerson(
            HttpServletRequest httpServletRequest,
            @RequestBody PersonPOJO personPOJO
    ) {
        if (httpServletRequest.getHeader("Authorization") == null) {
            return ResponseEntity.status(401).body(new PersonResponse("Unauthorized", null));
        }
        if (personPOJO.firstName() == null ||
                personPOJO.lastName() == null ||
                personPOJO.middleName() == null ||
                personPOJO.email() == null) {
            return ResponseEntity.status(400).body(new PersonResponse("Invalid person data\n" + personPOJO, null));
        }

        if (personService.getPersonByEmail(personPOJO.email()).isPresent()) {
            return ResponseEntity.status(400).body(new PersonResponse("Person with this email already exists", null));
        }
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String login = jwtUtils.extractLogin(token);
        if (login == null) {
            return ResponseEntity.status(401).body(new PersonResponse("Unauthorized", null));
        }
        User user = userService.getUserByLogin(login);
        if (user == null) {
            return ResponseEntity.status(401).body(new PersonResponse("Unauthorized", null));
        }
        Person person = personService.createPerson(personPOJO);
        person.setUsers(new ArrayList<>(List.of(user)));
        personService.savePerson(person);
        return ResponseEntity.ok(new PersonResponse("Person created", personPOJO));
    }


    @Getter
    @Setter
    public static class PersonResponse {
        String msg;
        PersonPOJO person;

        public PersonResponse(String msg, PersonPOJO person) {
            this.msg = msg;
            this.person = person;
        }
    }
}
