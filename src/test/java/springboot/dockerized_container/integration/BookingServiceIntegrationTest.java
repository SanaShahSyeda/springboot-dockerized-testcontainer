package springboot.dockerized_container.integration;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import springboot.dockerized_container.TestcontainersConfiguration;
import springboot.dockerized_container.dto.BookingSummary;
import springboot.dockerized_container.service.BookingService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Proves the N+1 bug with a real Postgres (Testcontainers) instead of eyeballing
 * SQL logs: {@link BookingService#getBookingSummaries()} fires 1 query for all
 * bookings plus 1 more per booking to lazily load its items (4 seeded bookings
 * -> 5 queries). {@link BookingService#getBookingSummariesOptimized()} uses a
 * {@code JOIN FETCH} and collapses that to a single query.
 */
@SpringBootTest(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
@Import(TestcontainersConfiguration.class)
class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Statistics statistics;

    @BeforeEach
    void resetStatistics() {
        statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.clear();
    }

    @Test
    void unoptimizedSummaries_triggerNPlusOneQueries() {
        List<BookingSummary> summaries = bookingService.getBookingSummaries();

        assertThat(summaries).hasSize(4);
        // 1 query for the bookings + 1 per booking to lazily load its items
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(5);
    }

    @Test
    void optimizedSummaries_useASingleJoinFetchQuery() {
        List<BookingSummary> summaries = bookingService.getBookingSummariesOptimized();

        assertThat(summaries).hasSize(4);
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }
}