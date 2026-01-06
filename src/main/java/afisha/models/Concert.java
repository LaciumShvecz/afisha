package afisha.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Название концерта не может быть пустым")
    @Column(nullable = false, length = 100)
    private String title;

    @NotNull(message = "Дата концерта не может быть пустой")
    @Column(nullable = false)
    private LocalDate date;

    @NotEmpty(message = "Место проведения не может быть пустым")
    @Column(nullable = false, length = 100)
    private String location;

    @NotNull(message = "Цена не может быть пустой")
    @Column(nullable = false)
    private Double price;

    @Column(length = 255)
    private String image;

    public Concert() {
    }

    public Concert(String title, LocalDate date, String location, Double price, String image) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.price = price;
        this.image = image;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
