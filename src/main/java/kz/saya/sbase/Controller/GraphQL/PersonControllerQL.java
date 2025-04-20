package kz.saya.sbase.Controller.GraphQL;

import kz.saya.sbase.Entity.Person;
import kz.saya.sbase.Repos.PersonRepository;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PersonControllerQL {
    private final PersonRepository personRepository;

    public PersonControllerQL(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @QueryMapping
    public List<Person> allPersons(){
        return personRepository.findAll();
    }
}
