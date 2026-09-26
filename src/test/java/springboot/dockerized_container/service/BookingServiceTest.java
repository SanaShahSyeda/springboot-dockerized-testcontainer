package springboot.dockerized_container.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import springboot.dockerized_container.domain.Booking;
import springboot.dockerized_container.domain.BookingItem;
import springboot.dockerized_container.dto.BookingSummary;
import springboot.dockerized_container.repository.BookingRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking bookingWithItems() {
        Booking booking = new Booking("Alice Johnson", LocalDate.of(2026, 1, 5));
        booking.addItem(new BookingItem("Desk", 1, new BigDecimal("199.99")));
        booking.addItem(new BookingItem("Chair", 2, new BigDecimal("89.50")));
        return booking;
    }

    @Test
    void getBookingSummaries_sumsLineTotalsPerBooking() {
        when(bookingRepository.findAll()).thenReturn(List.of(bookingWithItems()));

        List<BookingSummary> summaries = bookingService.getBookingSummaries();

        assertThat(summaries).hasSize(1);
        BookingSummary summary = summaries.get(0);
        assertThat(summary.customerName()).isEqualTo("Alice Johnson");
        // 1 * 199.99 + 2 * 89.50 = 378.99
        assertThat(summary.total()).isEqualByComparingTo("378.99");
        verify(bookingRepository).findAll();
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getBookingSummariesOptimized_usesJoinFetchQuery() {
        when(bookingRepository.findAllWithItems()).thenReturn(List.of(bookingWithItems()));

        List<BookingSummary> summaries = bookingService.getBookingSummariesOptimized();

        assertThat(summaries).hasSize(1);
        assertThat(summaries.get(0).total()).isEqualByComparingTo("378.99");
        verify(bookingRepository).findAllWithItems();
        verifyNoMoreInteractions(bookingRepository);
    }
}