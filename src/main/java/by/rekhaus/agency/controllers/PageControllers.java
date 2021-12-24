package by.rekhaus.agency.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageControllers {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Главная страница");
        return "index";
    }
    @GetMapping("/index_admin")
    public String index_admin(Model model) {
        model.addAttribute("title", "Главная страница");
        return "admin/index_admin";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "О нас");
        return "about";
    }

    @GetMapping("/contact")
    public String contacts(Model model) {
        model.addAttribute("title", "Контакты");
        return "contact";
    }

    @GetMapping("/ProfessionalPhotography")
    public String ProfessionalPhotography(Model model) {
        model.addAttribute("title", "Профессиональная фотосъемка");
        return "ProfessionalPhotography";
    }


}
