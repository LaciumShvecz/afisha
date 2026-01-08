package afisha.controllers;

import afisha.models.Concert;
import afisha.services.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class ConcertController {

    private final ConcertService concertService;

    @Autowired
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
        Concert concert = concertService.getConcertById(id);
        if (concert != null) {
            return ResponseEntity.ok(concert);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/api/concerts")
    public ResponseEntity<Concert> createConcert(@RequestBody Concert concert) {
        Concert savedConcert = concertService.saveConcert(concert);
        if (savedConcert != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedConcert);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/api/concerts/{id}")
    public ResponseEntity<Concert> updateConcert(@PathVariable Long id, @RequestBody Concert concert) {
        Concert existingConcert = concertService.getConcertById(id);
        if (existingConcert != null) {
            // Обновляем поля
            if (concert.getTitle() != null) {
                existingConcert.setTitle(concert.getTitle());
            }
            if (concert.getDescription() != null) {
                existingConcert.setDescription(concert.getDescription());
            }
            if (concert.getArtist() != null) {
                existingConcert.setArtist(concert.getArtist());
            }
            if (concert.getConcertDateTime() != null) {
                existingConcert.setConcertDateTime(concert.getConcertDateTime());
            }
            if (concert.getVenueName() != null) {
                existingConcert.setVenueName(concert.getVenueName());
            }
            if (concert.getVenueAddress() != null) {
                existingConcert.setVenueAddress(concert.getVenueAddress());
            }
            if (concert.getCity() != null) {
                existingConcert.setCity(concert.getCity());
            }
            if (concert.getPrice() != null) {
                existingConcert.setPrice(concert.getPrice());
            }
            if (concert.getCurrency() != null) {
                existingConcert.setCurrency(concert.getCurrency());
            }
            if (concert.getImageUrl() != null) {
                existingConcert.setImageUrl(concert.getImageUrl());
            }
            if (concert.getAvailableTickets() != null) {
                existingConcert.setAvailableTickets(concert.getAvailableTickets());
            }
            if (concert.getTotalTickets() != null) {
                existingConcert.setTotalTickets(concert.getTotalTickets());
            }

            Concert updatedConcert = concertService.saveConcert(existingConcert);
            return ResponseEntity.ok(updatedConcert);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/api/concerts/{id}")
    public ResponseEntity<Void> deleteConcert(@PathVariable Long id) {
        Concert concert = concertService.getConcertById(id);
        if (concert != null) {
            concertService.deleteConcert(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
        Concert concert = concertService.getConcertById(id);

        if (concert != null) {
            ModelAndView mav = new ModelAndView("concert");
            mav.addObject("concert", concert);
            mav.addObject("formattedDate", formatDate(concert.getConcertDateTime()));
            mav.addObject("formattedTime", formatTime(concert.getConcertDateTime()));
            return mav;
        } else {
            return new ModelAndView("redirect:/concerts");
        }
    }

    // Дополнительные API методы
    @GetMapping("/api/concerts/featured")
    public List<Concert> getFeaturedConcerts() {
        return concertService.getAllConcerts().stream()
                .filter(concert -> concert.isFeatured())
                .toList();
    }

    @GetMapping("/api/concerts/upcoming")
    public List<Concert> getUpcomingConcerts() {
        List<Concert> concerts = concertService.getAllConcerts();
        LocalDateTime now = LocalDateTime.now();

        return concerts.stream()
                .filter(concert -> concert.getConcertDateTime() != null &&
                        concert.getConcertDateTime().isAfter(now))
                .toList();
    }

    @GetMapping("/api/concerts/search")
    public List<Concert> searchConcerts(@RequestParam String query) {
        List<Concert> concerts = concertService.getAllConcerts();
        String lowerQuery = query.toLowerCase();

        return concerts.stream()
                .filter(concert ->
                        (concert.getTitle() != null && concert.getTitle().toLowerCase().contains(lowerQuery)) ||
                                (concert.getArtist() != null && concert.getArtist().toLowerCase().contains(lowerQuery)) ||
                                (concert.getDescription() != null && concert.getDescription().toLowerCase().contains(lowerQuery)) ||
                                (concert.getVenueName() != null && concert.getVenueName().toLowerCase().contains(lowerQuery)) ||
                                (concert.getCity() != null && concert.getCity().toLowerCase().contains(lowerQuery))
                )
                .toList();
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