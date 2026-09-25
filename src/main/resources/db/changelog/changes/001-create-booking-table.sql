--liquibase formatted sql

--changeset sana:001-create-booking-table
CREATE TABLE booking (
                         id BIGSERIAL PRIMARY KEY,
                         reference VARCHAR(64) NOT NULL UNIQUE,
                         customer_name VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT now()
);

--rollback DROP TABLE booking;