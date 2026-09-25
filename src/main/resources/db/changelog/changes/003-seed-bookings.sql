--liquibase formatted sql

--changeset sana:003-seed-bookings
INSERT INTO booking (reference, customer_name) VALUES
                                                   ('BK-1001', 'Alice Johnson'),
                                                   ('BK-1002', 'Brian Smith'),
                                                   ('BK-1003', 'Carla Diaz'),
                                                   ('BK-1004', 'David Chen'),
                                                   ('BK-1005', 'Emma Wilson');

--rollback DELETE FROM booking WHERE reference IN ('BK-1001', 'BK-1002', 'BK-1003', 'BK-1004', 'BK-1005');