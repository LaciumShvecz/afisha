package afisha.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название концерта обязательно")
    @Size(min = 3, max = 200, message = "Название должно быть от 3 до 200 символов")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Исполнитель обязателен")
    @Size(min = 2, max = 100, message = "Исполнитель должен быть от 2 до 100 символов")
    private String artist;

    @NotNull(message = "Дата и время обязательны")
    @Future(message = "Дата концерта должна быть в будущем")
    private LocalDateTime concertDateTime;

    @NotBlank(message = "Название места проведения обязательно")
    @Size(min = 2, max = 150, message = "Название места должно быть от 2 до 150 символов")
    private String venueName;

    @NotBlank(message = "Адрес обязателен")
    @Size(min = 5, max = 200, message = "Адрес должен быть от 5 до 200 символов")
    private String venueAddress;

    @NotBlank(message = "Город обязателен")
    @Size(min = 2, max = 50, message = "Город должен быть от 2 до 50 символов")
    private String city;

    @NotNull(message = "Минимальная цена обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
    private BigDecimal price;

    private String currency = "BYN"; // По умолчанию белорусские рубли

    @Column(length = 1000)
    private String additionalInfo;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_featured")
    private boolean featured = false;

    @Column(name = "available_tickets")
    private Integer availableTickets;

    @Column(name = "total_tickets")
    private Integer totalTickets;

    // Конструкторы
    public Concert() {
    }

    public Concert(String title, String artist, LocalDateTime concertDateTime,
                   String venueName, String venueAddress, BigDecimal price) {
        this.title = title;
        this.artist = artist;
        this.concertDateTime = concertDateTime;
        this.venueName = venueName;
        this.venueAddress = venueAddress;
        this.price = price;
    }

    // Вспомогательные методы
    public String getFormattedDateTime() {
        // Форматированная дата для отображения
        return concertDateTime != null ?
                concertDateTime.toString() : ""; // Можно использовать DateTimeFormatter
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public LocalDateTime getConcertDateTime() {
        return concertDateTime;
    }

    public void setConcertDateTime(LocalDateTime concertDateTime) {
        this.concertDateTime = concertDateTime;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public Integer getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(Integer availableTickets) {
        this.availableTickets = availableTickets;
    }

    public Integer getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(Integer totalTickets) {
        this.totalTickets = totalTickets;
    }
}