package afisha.controllers;

import afisha.models.User;
import afisha.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        System.out.println("Показать форму регистрации");
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult,
                               Model model) {

        System.out.println("Обработка регистрации пользователя: " + user.getUsername());

        try {
            // Проверка уникальности username
            if (registrationService.usernameExists(user.getUsername())) {
                System.out.println("Валидация: username уже существует");
                bindingResult.rejectValue("username", "error.user",
                        "Пользователь с таким именем уже существует");
            }

            // Проверка уникальности email
            if (registrationService.emailExists(user.getEmail())) {
                System.out.println("Валидация: email уже существует");
                bindingResult.rejectValue("email", "error.user",
                        "Пользователь с таким email уже существует");
            }

            if (bindingResult.hasErrors()) {
                System.out.println("Ошибки валидации: " + bindingResult.getAllErrors());
                return "register";
            }

            System.out.println("Валидация пройдена, регистрируем пользователя...");

            // Регистрация пользователя
            registrationService.registerUser(user);

            System.out.println("Регистрация успешна, перенаправляем на login");
            return "redirect:/login?registered=true";

        } catch (RuntimeException e) {
            System.out.println("Ошибка при регистрации: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Произошла ошибка при регистрации");
            return "register";
        }
    }
}