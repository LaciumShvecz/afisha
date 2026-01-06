package afisha.models;

import lombok.*;
import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Пользователь, который сделал бронирование
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Концерт, который бронируется
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    // Количество билетов
    @Column(nullable = false)
    private Integer quantity = 1;

    // Итоговая цена
    @Column(nullable = false)
    private Double totalPrice;

    // Дата бронирования
    @Column(nullable = false)
    private LocalDateTime bookingDate = LocalDateTime.now();
}
