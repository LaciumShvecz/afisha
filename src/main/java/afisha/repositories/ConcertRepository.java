package afisha.repositories;

import afisha.models.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    List<Concert> findByArtistContainingIgnoreCase(String artist);
    List<Concert> findByTitleContainingIgnoreCase(String title);
    List<Concert> findByCity(String city);
    List<Concert> findByConcertDateTimeAfter(LocalDateTime date);
    List<Concert> findByFeaturedTrue();

    @Query("SELECT c FROM Concert c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.artist) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.venueName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Concert> searchConcerts(@Param("query") String query);

    List<Concert> findAllByOrderByConcertDateTimeAsc();
    List<Concert> findAllByOrderByConcertDateTimeDesc();

    @Modifying
    @Query("UPDATE Concert c SET c.availableTickets = c.availableTickets - :quantity WHERE c.id = :concertId AND c.availableTickets >= :quantity")
    int decreaseAvailableTickets(@Param("concertId") Long concertId,
                                 @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE Concert c SET c.availableTickets = c.availableTickets + :quantity WHERE c.id = :concertId")
    int increaseAvailableTickets(@Param("concertId") Long concertId,
                                 @Param("quantity") Integer quantity);

    // Метод для получения только количества доступных билетов
    @Query("SELECT c.availableTickets FROM Concert c WHERE c.id = :concertId")
    Integer findAvailableTicketsById(@Param("concertId") Long concertId);

    // Метод для поиска концертов по городу
    List<Concert> findByCityContainingIgnoreCase(String city);


}
