package kz.saya.sbasecore.Service;

import kz.saya.sbasecore.Entity.Person;
import kz.saya.sbasecore.POJOs.PersonPOJO;
import kz.saya.sbasecore.Repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person createPerson(PersonPOJO personPOJO){
        Person person = new Person();
        person.setFirstName(personPOJO.firstName());
        person.setLastName(personPOJO.lastName());
        person.setMiddleName(personPOJO.middleName());
        person.setEmail(personPOJO.email());
        return personRepository.save(person);
    }

    public Person getPersonById(UUID id){
        return personRepository.findById(id).orElse(null);
    }
    public Optional<Person> getPersonByEmail(String email){
        return personRepository.findByEmail(email);
    }
    public Person getPersonByUser(String login){
        return personRepository.findByUsersLogin(login).orElse(null);
    }

    public Person savePerson(Person person){
        return personRepository.save(person);
    }


}
