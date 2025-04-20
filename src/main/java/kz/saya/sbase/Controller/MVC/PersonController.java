package kz.saya.sbase.Controller.MVC;

import kz.saya.sbase.Entity.Interfaces.Menuable;
import kz.saya.sbase.Entity.Person;
import kz.saya.sbase.Repos.PersonRepository;
import kz.saya.sbase.Service.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Menuable(
        caption = "Пользователи",
        icon = "fa-solid fa-user",
        order = 1,
        parent = "/admin"
)
@Controller
public class PersonController {

    private final PersonService personService;
    private final PersonRepository personRepository;

    public PersonController(PersonService personService, PersonRepository personRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
    }

    @GetMapping("/person")
    public String showPersonList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());
        Page<Person> pageResult = personRepository.findAll(pageable);

        model.addAttribute("persons", pageResult.getContent());
        model.addAttribute("currentPage", pageResult.getNumber());
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("size", size);

        return "person";
    }

}
