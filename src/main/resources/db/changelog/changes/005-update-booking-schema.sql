--liquibase formatted sql

--changeset sana:005-update-booking-schema
ALTER TABLE booking DROP COLUMN IF EXISTS reference;
ALTER TABLE booking DROP COLUMN IF EXISTS created_at;
ALTER TABLE booking ADD COLUMN IF NOT EXISTS booking_date DATE NOT NULL DEFAULT CURRENT_DATE;

ALTER TABLE booking_item RENAME COLUMN item_name TO product_name;

--rollback ALTER TABLE booking_item RENAME COLUMN product_name TO item_name;
--rollback ALTER TABLE booking DROP COLUMN IF EXISTS booking_date;
--rollback ALTER TABLE booking ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NOT NULL DEFAULT now();
--rollback ALTER TABLE booking ADD COLUMN IF NOT EXISTS reference VARCHAR(64);
