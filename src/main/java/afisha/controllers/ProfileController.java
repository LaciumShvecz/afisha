package afisha.controllers;

import afisha.models.Booking;
import afisha.models.User;
import afisha.services.BookingService;
import afisha.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/user")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        User user = userService.getUserByUsername(username);

        if (user != null) {
            List<Booking> bookings = bookingService.getBookingsByUser(user);

            // Рассчитываем статистику
            double totalAmount = 0.0;
            int totalTickets = 0;
            LocalDateTime lastBookingDate = null;

            if (!bookings.isEmpty()) {
                for (Booking booking : bookings) {
                    if (booking.getTotalAmount() != null) {
                        totalAmount += booking.getTotalAmount();
                    }
                    if (booking.getTicketQuantity() != null) {
                        totalTickets += booking.getTicketQuantity();
                    }
                    if (booking.getBookingDate() != null) {
                        if (lastBookingDate == null ||
                                booking.getBookingDate().isAfter(lastBookingDate)) {
                            lastBookingDate = booking.getBookingDate();
                        }
                    }
                }
            }

            model.addAttribute("user", user);
            model.addAttribute("bookings", bookings);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("totalTickets", totalTickets);
            model.addAttribute("lastBookingDate", lastBookingDate);

            return "user";
        } else {
            // Если пользователь не найден, редирект на главную
            return "redirect:/";
        }
    }


    @PostMapping("/user/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        User user = userService.getUserByUsername(username);

        if (user != null) {
            try {
                // Используем метод с одним параметром, как в BookingService
                bookingService.cancelBooking(id);
            } catch (Exception e) {
                // Можно добавить flash сообщение об ошибке
                System.err.println("Ошибка при отмене бронирования: " + e.getMessage());
                // Логирование ошибки
                e.printStackTrace();
            }
        }

        return "redirect:/profile";
    }
    @GetMapping("/profile")
    public String showProfile() {
        // Редирект с /profile на /user
        return "redirect:/user";
    }
}