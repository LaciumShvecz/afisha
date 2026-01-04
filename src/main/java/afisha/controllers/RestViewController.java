package afisha.controllers;

import org.springframework.stereotype.Controller;
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
}
