package afisha.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestViewController {
    @GetMapping("/admin")
    public String showAdmin() {
        return "admin";
    }

    @GetMapping("/user")
    public String showUser() {
        return "user";
    }

    /*@GetMapping("/index")
    public String showIndex() {
        return "index";
    }*/

    @GetMapping("/concert")
    public String showConcert() {
        return "concert";
    }

    @GetMapping("/ticket")
    public String showTicket() {
        return "ticket";
    }
    @GetMapping("/admin-concerts")
    public String showAdminConcerts() {
        return "admin-concerts";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
