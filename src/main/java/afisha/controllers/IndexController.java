package afisha.controllers;

import afisha.models.Concert;
import afisha.services.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ConcertService concertService;

    @GetMapping({"", "/", "/index"})
    public String homePage(Model model) {
        try {
            // Получаем все концерты
            List<Concert> allConcerts = concertService.getAllConcerts();

            // Если хотите только предстоящие, используйте:
            // List<Concert> allConcerts = concertService.getUpcomingConcerts();

            System.out.println("Загружено концертов: " + allConcerts.size());

            // Ограничиваем до 6 концертов для главной страницы
            List<Concert> concerts = allConcerts.size() > 6 ?
                    allConcerts.subList(0, 6) : allConcerts;

            model.addAttribute("concerts", concerts);
            return "index";
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке концертов: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("concerts", List.of()); // Пустой список
            return "index";
        }
    }
}