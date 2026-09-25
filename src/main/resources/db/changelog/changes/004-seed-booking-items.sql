--liquibase formatted sql

--changeset sana:004-seed-booking-items
INSERT INTO booking_item (booking_id, item_name, quantity, unit_price)
SELECT b.id, v.item_name, v.quantity, v.unit_price
FROM booking b
         JOIN (VALUES
                   ('BK-1001', 'Standard Room', 2, 120.00),
                   ('BK-1001', 'Breakfast', 2, 15.00),
                   ('BK-1001', 'Airport Transfer', 1, 35.00),
                   ('BK-1002', 'Deluxe Room', 1, 220.00),
                   ('BK-1002', 'Spa Voucher', 1, 60.00),
                   ('BK-1002', 'Late Checkout', 1, 25.00),
                   ('BK-1003', 'Standard Room', 1, 120.00),
                   ('BK-1003', 'Breakfast', 1, 15.00),
                   ('BK-1003', 'Parking', 3, 10.00),
                   ('BK-1004', 'Suite', 1, 350.00),
                   ('BK-1004', 'Breakfast', 2, 15.00),
                   ('BK-1004', 'Airport Transfer', 2, 35.00),
                   ('BK-1005', 'Standard Room', 3, 120.00),
                   ('BK-1005', 'Spa Voucher', 2, 60.00),
                   ('BK-1005', 'Late Checkout', 1, 25.00)
) AS v (reference, item_name, quantity, unit_price)
              ON v.reference = b.reference;

--rollback DELETE FROM booking_item;