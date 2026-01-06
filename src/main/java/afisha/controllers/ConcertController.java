package afisha.controllers;

import afisha.models.Concert;
import afisha.services.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
public class ConcertController {

    private final ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    // API методы (возвращают JSON)
    @GetMapping("/api/concerts")
    public List<Concert> getAllConcerts() {
        return concertService.getAllConcerts();
    }

    @GetMapping("/api/concerts/{id}")
    public ResponseEntity<Concert> getConcertById(@PathVariable Long id) {
        Optional<Concert> concert = concertService.getConcertById(id);
        return concert.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // HTML методы (возвращают ModelAndView)
    @GetMapping("/concerts")
    public ModelAndView getConcertsPage() {
        ModelAndView mav = new ModelAndView("concerts");
        mav.addObject("concerts", concertService.getAllConcerts());
        return mav;
    }

    @GetMapping("/concert/{id}")
    public ModelAndView getConcertPage(@PathVariable Long id) {
        Optional<Concert> concert = concertService.getConcertById(id);

        if (concert.isPresent()) {
            ModelAndView mav = new ModelAndView("concert");
            mav.addObject("concert", concert.get());
            mav.addObject("formattedDate", formatDate(concert.get().getConcertDateTime()));
            mav.addObject("formattedTime", formatTime(concert.get().getConcertDateTime()));
            return mav;
        } else {
            return new ModelAndView("redirect:/concerts");
        }
    }

    // Вспомогательные методы
    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'года'");
        return dateTime.format(formatter);
    }

    private String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }
}