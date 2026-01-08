package afisha.controllers;

import afisha.models.Booking;
import afisha.models.Concert;
import afisha.models.User;
import afisha.services.BookingService;
import afisha.services.ConcertService;
import afisha.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/ticket/{concertId}")
    public String showBookingPage(@PathVariable Long concertId, Model model) {
        Concert concert = concertService.getConcertById(concertId);

        if (concert != null) {
            model.addAttribute("concert", concert);

            Booking booking = new Booking();

            // Устанавливаем значения по умолчанию
            booking.setConcert(concert);
            booking.setTicketQuantity(1); // Устанавливаем значение по умолчанию

            // Конвертируем BigDecimal в Double
            if (concert.getPrice() != null) {
                booking.setPricePerTicket(concert.getPrice().doubleValue());
            }

            // Автозаполнение данных пользователя, если он авторизован
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                User user = userService.getUserByUsername(username);

                if (user != null) {
                    booking.setCustomerName(user.getName());
                    booking.setCustomerEmail(user.getEmail());
                    booking.setUser(user);
                }
            }

            model.addAttribute("booking", booking);
            return "ticket";
        }

        return "redirect:/";
    }

    @PostMapping("/bookings/create")
    public String createBooking(@ModelAttribute("booking") @Valid Booking booking,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            // Если есть ошибки валидации, загружаем концерт заново
            if (booking.getConcert() != null && booking.getConcert().getId() != null) {
                Concert concert = concertService.getConcertById(booking.getConcert().getId());
                if (concert != null) {
                    model.addAttribute("concert", concert);
                }
            }
            return "ticket";
        }

        try {
            // Устанавливаем пользователя
            User user = null;
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                user = userService.getUserByUsername(username);
            }

            // Расчет итоговой суммы
            booking.calculateTotalAmount();

            // Сохраняем бронирование
            Booking savedBooking = bookingService.createBooking(booking, user);

            return "redirect:/bookings/success/" + savedBooking.getBookingNumber();

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());

            // Загружаем концерт для отображения
            if (booking.getConcert() != null && booking.getConcert().getId() != null) {
                Concert concert = concertService.getConcertById(booking.getConcert().getId());
                if (concert != null) {
                    model.addAttribute("concert", concert);
                }
            }

            return "ticket";
        }
    }

    @GetMapping("/bookings/success/{bookingNumber}")
    public String showSuccessPage(@PathVariable String bookingNumber, Model model) {
        Booking booking = bookingService.getBookingByNumber(bookingNumber);

        if (booking != null) {
            model.addAttribute("booking", booking);
            return "booking-success";
        }

        return "redirect:/";
    }
}