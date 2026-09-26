package springboot.dockerized_container.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import springboot.dockerized_container.domain.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select distinct b from Booking b join fetch b.items")
    List<Booking> findAllWithItems();
}