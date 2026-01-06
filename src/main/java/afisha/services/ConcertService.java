package afisha.services;

import afisha.models.Concert;
import afisha.repositories.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConcertService {

    @Autowired
    private ConcertRepository concertRepository;

    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    public Optional<Concert> getConcertById(Long id) {
        return concertRepository.findById(id);
    }

    public Concert saveConcert(Concert concert) {
        return concertRepository.save(concert);
    }

    public void deleteConcert(Long id) {
        concertRepository.deleteById(id);
    }

    public List<Concert> getUpcomingConcerts() {
        return concertRepository.findByConcertDateTimeAfter(LocalDateTime.now());
    }

    public List<Concert> getFeaturedConcerts() {
        return concertRepository.findByFeaturedTrue();
    }

    public List<Concert> searchConcerts(String query) {
        List<Concert> byArtist = concertRepository.findByArtistContainingIgnoreCase(query);
        List<Concert> byTitle = concertRepository.findByTitleContainingIgnoreCase(query);

        // Объединяем результаты, исключая дубликаты
        byArtist.addAll(byTitle.stream()
                .filter(concert -> !byArtist.contains(concert))
                .toList());

        return byArtist;
    }

    public List<Concert> getConcertsByCity(String city) {
        return concertRepository.findByCity(city);
    }
}