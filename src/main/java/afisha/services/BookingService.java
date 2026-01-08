package afisha.services;

import afisha.models.Booking;
import afisha.models.Concert;
import afisha.models.User;
import afisha.repositories.BookingRepository;
import afisha.repositories.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ConcertService concertService;
    @Autowired
    private ConcertRepository concertRepository;


    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking getBookingByNumber(String bookingNumber) {
        return bookingRepository.findByBookingNumber(bookingNumber);
    }

    public List<Booking> getBookingsForConcert(Long concertId) {
        Concert concert = concertService.getConcertById(concertId);
        if (concert != null) {
            return bookingRepository.findByConcert(concert);
        }
        return List.of();
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking != null) {
            Concert concert = booking.getConcert();

            if (concert.getAvailableTickets() != null) {
                concert.setAvailableTickets(
                        concert.getAvailableTickets() + booking.getTicketQuantity()
                );
                concertService.saveConcert(concert);
            }

            bookingRepository.delete(booking);
        }
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public Long countBookingsForConcert(Long concertId) {
        Concert concert = concertService.getConcertById(concertId);
        if (concert != null) {
            return bookingRepository.countByConcert(concert);
        }
        return 0L;
    }

    @Transactional
    public Booking createBooking(Booking booking, User user) {
        // Устанавливаем пользователя
        booking.setUser(user);

        // Убедимся, что totalAmount рассчитан
        if (booking.getTotalAmount() == null && booking.getPricePerTicket() != null) {
            booking.setTotalAmount(booking.getPricePerTicket() * booking.getTicketQuantity());
        }

        // Получаем актуальный концерт из базы
        Concert concert = concertService.getConcertById(booking.getConcert().getId());
        booking.setConcert(concert);

        // Проверяем доступность билетов
        if (concert.getAvailableTickets() != null) {
            if (concert.getAvailableTickets() < booking.getTicketQuantity()) {
                throw new RuntimeException("Недостаточно билетов. Доступно: " +
                        concert.getAvailableTickets());
            }

            // Обновляем количество доступных билетов
            int newAvailable = concert.getAvailableTickets() - booking.getTicketQuantity();
            concert.setAvailableTickets(newAvailable);

            // Сохраняем обновленный концерт
            concertService.saveConcert(concert);
        }

        // Сохраняем бронирование
        return bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking != null) {
            // Проверяем права на отмену (если пользователь указан)
            if (user != null && booking.getUser() != null &&
                    !booking.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Вы не можете отменить чужое бронирование");
            }

            Concert concert = booking.getConcert();

            // Возвращаем билеты в доступные
            if (concert.getAvailableTickets() != null) {
                int newAvailable = concert.getAvailableTickets() + booking.getTicketQuantity();
                concert.setAvailableTickets(newAvailable);
                concertService.saveConcert(concert);
            }

            // Удаляем бронирование
            bookingRepository.delete(booking);
        }
    }
    // В BookingService добавьте метод для получения всех бронирований с концертами
    public List<Booking> getBookingsByUserWithConcert(User user) {
        List<Booking> bookings = bookingRepository.findByUser(user);

        // Если концерты лениво загружены, инициализируем их
        if (bookings != null) {
            bookings.forEach(booking -> {
                if (booking.getConcert() != null) {
                    // Инициализируем концерт (если ленивая загрузка)
                    booking.getConcert().getId(); // Достаточно вызвать любой геттер
                }
            });
        }

        return bookings;
    }
}