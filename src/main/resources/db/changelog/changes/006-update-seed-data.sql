--liquibase formatted sql

--changeset sana:006-update-seed-data
DELETE FROM booking_item;
DELETE FROM booking;

INSERT INTO booking (id, customer_name, booking_date) VALUES
    (1, 'Alice Johnson', '2026-01-05'),
    (2, 'Brian Lee', '2026-01-06'),
    (3, 'Carla Diaz', '2026-01-07'),
    (4, 'David Kim', '2026-01-08');

INSERT INTO booking_item (id, product_name, quantity, unit_price, booking_id) VALUES
    (1, 'Desk', 1, 199.99, 1),
    (2, 'Chair', 2, 89.50, 1),
    (3, 'Monitor', 2, 249.00, 2),
    (4, 'Keyboard', 1, 45.00, 2),
    (5, 'Lamp', 3, 25.00, 3),
    (6, 'Bookshelf', 1, 150.00, 3),
    (7, 'Webcam', 1, 60.00, 4),
    (8, 'Headset', 2, 35.00, 4);

--rollback DELETE FROM booking_item WHERE id IN (1, 2, 3, 4, 5, 6, 7, 8);
--rollback DELETE FROM booking WHERE id IN (1, 2, 3, 4);
