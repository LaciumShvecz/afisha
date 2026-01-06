package afisha.repositories;

import afisha.models.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    List<Concert> findByArtistContainingIgnoreCase(String artist);

    List<Concert> findByTitleContainingIgnoreCase(String title);

    List<Concert> findByCity(String city);

    List<Concert> findByConcertDateTimeAfter(LocalDateTime date);

    List<Concert> findByFeaturedTrue();

    List<Concert> findAllByOrderByConcertDateTimeAsc();

    List<Concert> findAllByOrderByConcertDateTimeDesc();
}