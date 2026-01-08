package afisha.services;

import afisha.models.Concert;
import afisha.repositories.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConcertService {

    @Autowired
    private ConcertRepository concertRepository;

    public Concert getConcertById(Long id) {
        return concertRepository.findById(id).orElse(null);
    }


    public List<Concert> getFeaturedConcerts() {
        return concertRepository.findByFeaturedTrue();
    }

    public List<Concert> searchConcerts(String query) {
        return concertRepository.searchConcerts(query);
    }

    @Transactional
    public void updateAvailableTickets(Long concertId, int quantityChange) {
        Concert concert = getConcertById(concertId);
        if (concert != null && concert.getAvailableTickets() != null) {
            int newAvailable = concert.getAvailableTickets() + quantityChange;
            concert.setAvailableTickets(Math.max(newAvailable, 0));
            concertRepository.save(concert);
        }
    }

    @Transactional
    public Concert saveConcert(Concert concert) {
        return concertRepository.save(concert);
    }

    @Transactional
    public void deleteConcert(Long id) {
        concertRepository.deleteById(id);
    }

    // Метод для проверки доступности билетов
    public boolean checkTicketsAvailability(Long concertId, int requestedTickets) {
        Concert concert = getConcertById(concertId);
        if (concert == null || concert.getAvailableTickets() == null) {
            return false;
        }
        return concert.getAvailableTickets() >= requestedTickets;
    }

    // Метод для резервирования билетов (можно использовать для атомарных операций)
    @Transactional
    public synchronized boolean reserveTickets(Long concertId, int tickets) {
        Concert concert = getConcertById(concertId);
        if (concert != null && concert.getAvailableTickets() != null) {
            if (concert.getAvailableTickets() >= tickets) {
                concert.setAvailableTickets(concert.getAvailableTickets() - tickets);
                concertRepository.save(concert);
                return true;
            }
        }
        return false;
    }

    // Метод для получения 6 ближайших концертов
    public List<Concert> getUpcomingConcerts() {
        LocalDateTime now = LocalDateTime.now();

        // Получаем все будущие концерты и сортируем по дате
        List<Concert> upcomingConcerts = concertRepository.findByConcertDateTimeAfter(now);

        // Сортируем по дате (от ближайших к дальним) и берем первые 6
        return upcomingConcerts.stream()
                .sorted((c1, c2) -> c1.getConcertDateTime().compareTo(c2.getConcertDateTime()))
                .limit(6)
                .collect(Collectors.toList());
    }

    // Метод для получения всех концертов
    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    // Метод для получения всех будущих концертов (для страницы расписания)
    public List<Concert> getAllUpcomingConcerts() {
        LocalDateTime now = LocalDateTime.now();
        return concertRepository.findByConcertDateTimeAfter(now);
    }



}