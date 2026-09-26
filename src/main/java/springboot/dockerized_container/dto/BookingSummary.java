package springboot.dockerized_container.dto;

import java.math.BigDecimal;

public record BookingSummary(Long bookingId, String customerName, BigDecimal total) {
}