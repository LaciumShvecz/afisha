package afisha.repositories;

import afisha.models.Booking;
import afisha.models.Concert;
import afisha.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);
    List<Booking> findByConcert(Concert concert);
    List<Booking> findByCustomerEmail(String email);
    Booking findByBookingNumber(String bookingNumber);
    Long countByConcert(Concert concert);
}