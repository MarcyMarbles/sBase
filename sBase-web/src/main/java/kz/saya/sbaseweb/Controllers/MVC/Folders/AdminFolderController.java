package kz.saya.sbaseweb.Controllers.MVC.Folders;

import kz.saya.sbaseweb.Entity.Interfaces.Menuable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Menuable(
        caption = "Администрирование",
        icon = "settings",
        order = 0,
        group = true
)
@Controller
@RequestMapping("/admin")
public class AdminFolderController extends AbstractFolder {}
