--liquibase formatted sql

--changeset sana:002-create-booking-item-table
CREATE TABLE booking_item (
                              id BIGSERIAL PRIMARY KEY,
                              booking_id BIGINT NOT NULL REFERENCES booking (id),
                              item_name VARCHAR(255) NOT NULL,
                              quantity INTEGER NOT NULL,
                              unit_price NUMERIC(10, 2) NOT NULL
);

CREATE INDEX idx_booking_item_booking_id ON booking_item (booking_id);

--rollback DROP TABLE booking_item;