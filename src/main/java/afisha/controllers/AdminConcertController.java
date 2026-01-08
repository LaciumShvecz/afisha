package afisha.controllers;

import afisha.models.Concert;
import afisha.services.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/concerts")
@PreAuthorize("hasRole('ADMIN')")
public class AdminConcertController {

    @Autowired
    private ConcertService concertService;

    @GetMapping
    public ResponseEntity<List<Concert>> getAllConcerts() {
        List<Concert> concerts = concertService.getAllConcerts();
        return ResponseEntity.ok(concerts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concert> getConcertById(@PathVariable Long id) {
        Concert concert = concertService.getConcertById(id);
        if (concert != null) {
            return ResponseEntity.ok(concert);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createConcert(@Valid @RequestBody Concert concert) {
        try {
            Concert savedConcert = concertService.saveConcert(concert);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedConcert);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateConcert(@PathVariable Long id, @Valid @RequestBody Concert concert) {
        try {
            concert.setId(id);
            Concert updatedConcert = concertService.saveConcert(concert);
            return ResponseEntity.ok(updatedConcert);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConcert(@PathVariable Long id) {
        try {
            concertService.deleteConcert(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}