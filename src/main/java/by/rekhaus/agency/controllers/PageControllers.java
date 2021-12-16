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

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "О нас");
        return "about";
    }
    @GetMapping("/contacts")
    public String contacts(Model model) {
        model.addAttribute("title", "Контакты");
        return "contacts";
    }
    @GetMapping("/ProfessionalPhotography")
    public String ProfessionalPhotography(Model model) {
        model.addAttribute("title", "Профессиональная фотосъемка");
        return "ProfessionalPhotography";
    }
    @GetMapping("/product")
    public String product(Model model) {
        model.addAttribute("title", "Продукты");
        return "product";
    }
    @GetMapping("/price")
    public String price(Model model) {
        model.addAttribute("title", "Цены");
        return "price";
    }
    @GetMapping("/portfolio")
    public String portfolio(Model model) {
        model.addAttribute("title", "Портфолио");
        return "portfolio";
    }
}
