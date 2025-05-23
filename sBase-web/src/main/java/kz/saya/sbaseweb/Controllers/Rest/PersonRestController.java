package kz.saya.sbaseweb.Controllers.Rest;

import jakarta.servlet.http.HttpServletRequest;
import kz.saya.sbasecore.Entity.Person;
import kz.saya.sbasecore.Entity.User;
import kz.saya.sbasecore.POJOs.PersonPOJO;
import kz.saya.sbasesecurity.Security.JwtUtils;
import kz.saya.sbasecore.Service.PersonService;
import kz.saya.sbasecore.Service.UserService;
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
public class PersonRestController {

    private final PersonService personService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public PersonRestController(PersonService personService, JwtUtils jwtUtils, UserService userService) {
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
        String login = jwtUtils.getUsernameFromToken(token);
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
