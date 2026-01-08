package afisha.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя обязательно")
    private String customerName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String customerEmail;

    @NotBlank(message = "Телефон обязателен")
    private String customerPhone;

    @NotNull(message = "Количество билетов обязательно")
    @Min(value = 1, message = "Минимум 1 билет")
    @Max(value = 10, message = "Максимум 10 билетов")
    private Integer ticketQuantity = 1; // Устанавливаем значение по умолчанию

    @NotNull(message = "Цена обязательна")
    private Double pricePerTicket;

    @NotNull(message = "Итоговая сумма обязательна")
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate = LocalDateTime.now();

    @Column(name = "booking_number", unique = true)
    private String bookingNumber;

    // Конструкторы
    public Booking() {
        // Генерация уникального номера бронирования
        this.bookingNumber = "BOOK-" + System.currentTimeMillis();
    }

    public Booking(String customerName, String customerEmail, String customerPhone,
                   Integer ticketQuantity, Double pricePerTicket, Concert concert, User user) {
        this();
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.ticketQuantity = ticketQuantity != null ? ticketQuantity : 1;
        this.pricePerTicket = pricePerTicket;
        this.totalAmount = pricePerTicket * this.ticketQuantity;
        this.concert = concert;
        this.user = user;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public Integer getTicketQuantity() { return ticketQuantity; }
    public void setTicketQuantity(Integer ticketQuantity) {
        this.ticketQuantity = ticketQuantity != null ? ticketQuantity : 1;
        // Обновляем totalAmount только если pricePerTicket установлен
        if (this.pricePerTicket != null) {
            this.totalAmount = this.pricePerTicket * this.ticketQuantity;
        }
    }

    public Double getPricePerTicket() { return pricePerTicket; }
    public void setPricePerTicket(Double pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
        // Обновляем totalAmount только если ticketQuantity установлен
        if (this.ticketQuantity != null) {
            this.totalAmount = pricePerTicket * this.ticketQuantity;
        } else {
            this.ticketQuantity = 1; // Устанавливаем значение по умолчанию
            this.totalAmount = pricePerTicket;
        }
    }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Concert getConcert() { return concert; }
    public void setConcert(Concert concert) { this.concert = concert; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public String getBookingNumber() { return bookingNumber; }
    public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }

    // Дополнительный метод для расчета итоговой суммы
    public void calculateTotalAmount() {
        if (this.pricePerTicket != null && this.ticketQuantity != null) {
            this.totalAmount = this.pricePerTicket * this.ticketQuantity;
        }
    }
}