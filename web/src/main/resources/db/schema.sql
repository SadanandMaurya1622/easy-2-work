-- Production-oriented schema: utf8mb4, InnoDB. Run once per environment.
CREATE DATABASE IF NOT EXISTS easy2work
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE easy2work;

CREATE TABLE IF NOT EXISTS site_stats (
  id TINYINT UNSIGNED NOT NULL PRIMARY KEY,
  homes_serviced INT UNSIGNED NOT NULL,
  hours_saved INT UNSIGNED NOT NULL,
  verified_professionals INT UNSIGNED NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO site_stats (id, homes_serviced, hours_saved, verified_professionals)
VALUES (1, 1200, 850, 50)
ON DUPLICATE KEY UPDATE
  homes_serviced = VALUES(homes_serviced),
  hours_saved = VALUES(hours_saved),
  verified_professionals = VALUES(verified_professionals);

CREATE TABLE IF NOT EXISTS service_booking (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customer_name VARCHAR(120) NOT NULL,
  phone VARCHAR(32) NOT NULL,
  email VARCHAR(255) NULL,
  service_type VARCHAR(64) NOT NULL,
  description TEXT NOT NULL,
  address TEXT NOT NULL,
  preferred_at DATETIME NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_service_booking_status_created (status, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- After job done (admin / backend): UPDATE service_booking SET status = 'COMPLETED' WHERE id = ?;
-- Cancel: status = 'CANCELLED'. My Booking page groups COMPLETED vs pending (PENDING, ACCEPTED, IN_PROGRESS, etc.).
