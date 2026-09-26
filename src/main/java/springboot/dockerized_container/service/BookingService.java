package springboot.dockerized_container.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.dockerized_container.domain.Booking;
import springboot.dockerized_container.domain.BookingItem;
import springboot.dockerized_container.dto.BookingSummary;
import springboot.dockerized_container.repository.BookingRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    /**
     * Kept intentionally: {@code findAll()} + accessing each booking's lazy
     * {@code items} triggers 1 query for the bookings plus 1 more per booking
     * (N+1). Used by a regression test to prove the bug stays caught.
     */
    @Transactional(readOnly = true)
    public List<BookingSummary> getBookingSummaries() {
        return bookingRepository.findAll().stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingSummary> getBookingSummariesOptimized() {
        return bookingRepository.findAllWithItems().stream()
                .map(this::toSummary)
                .toList();
    }

    private BookingSummary toSummary(Booking booking) {
        BigDecimal total = booking.getItems().stream()
                .map(BookingItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new BookingSummary(booking.getId(), booking.getCustomerName(), total);
    }
}