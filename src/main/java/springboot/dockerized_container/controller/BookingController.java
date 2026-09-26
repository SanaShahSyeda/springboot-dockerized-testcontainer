package springboot.dockerized_container.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.dockerized_container.dto.BookingSummary;
import springboot.dockerized_container.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/summary")
    public List<BookingSummary> summaries() {
        return bookingService.getBookingSummariesOptimized();
    }
}