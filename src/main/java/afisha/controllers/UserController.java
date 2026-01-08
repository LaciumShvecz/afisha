package afisha.controllers;

import afisha.models.Booking;
import afisha.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import afisha.models.Role;
import afisha.models.User;
import afisha.services.RoleServiceImpl;
import afisha.services.UserServiceImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class UserController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final BookingService bookingService;


    @Autowired
    public UserController(UserServiceImpl userService, RoleServiceImpl roleService, BookingService bookingService) {
        this.userService = userService;
        this.roleService = roleService;
        this.bookingService = bookingService;
    }

    @GetMapping()
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(userService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Collection<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }
    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        if (user != null) {
            List<Booking> bookings = bookingService.getBookingsByUser(user);

            long upcomingBookings = bookings.stream()
                    .filter(b -> {
                        LocalDateTime concertDate = b.getConcert().getConcertDateTime();
                        return concertDate != null && concertDate.isAfter(LocalDateTime.now());
                    })
                    .count();


            model.addAttribute("user", user);
            model.addAttribute("bookings", bookings);
            model.addAttribute("totalBookings", bookings.size());
            model.addAttribute("upcomingBookings", upcomingBookings);
        }

        return "profile";
    }
}