CREATE DATABASE travel_management_db;
USE travel_management_db;

CREATE TABLE flight (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(20) NOT NULL UNIQUE,
    airline_name VARCHAR(100) NOT NULL,
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    available_seats INT NOT NULL
);

#test
INSERT INTO flight
(flight_number, airline_name, origin, destination, departure_time, arrival_time, price, available_seats)
VALUES
('UL225', 'SriLankan Airlines', 'Colombo', 'Dubai',
 '2026-10-10 10:30:00', '2026-10-10 13:45:00',
 85000.00, 120),

('EK649', 'Emirates', 'Colombo', 'Dubai',
 '2026-10-11 03:15:00', '2026-10-11 06:20:00',
 92000.00, 80),

('QR665', 'Qatar Airways', 'Colombo', 'Doha',
 '2026-10-12 04:00:00', '2026-10-12 06:50:00',
 99000.00, 95);
 
 select * from flight;
 
 
 CREATE TABLE invoice (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_number VARCHAR(30) NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    invoice_date DATETIME NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    invoice_status VARCHAR(30) NOT NULL
);

CREATE TABLE payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    payment_date DATETIME NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    payment_status VARCHAR(30) NOT NULL,

    FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id)
);

#test
INSERT INTO invoice
(invoice_number, customer_name, invoice_date, total_amount, invoice_status)
VALUES
('INV-0001', 'Amal Perera', '2026-09-24 10:00:00', 250000.00, 'UNPAID'),
('INV-0002', 'Nimal Silva', '2026-09-24 11:30:00', 180000.00, 'PAID'),
('INV-0003', 'Kamal Fernando', '2026-09-24 14:00:00', 320000.00, 'PARTIAL');

INSERT INTO payment
(invoice_id, payment_date, amount, payment_method, payment_status)
VALUES
(2, '2026-09-24 12:00:00', 180000.00, 'CARD', 'COMPLETED'),
(3, '2026-09-24 15:00:00', 100000.00, 'BANK_TRANSFER', 'COMPLETED');

 SELECT * FROM invoice;

SELECT * FROM payment;

CREATE TABLE IF NOT EXISTS tour_package (
 package_id INT AUTO_INCREMENT PRIMARY KEY,
 package_name VARCHAR(150) NOT NULL,
 description VARCHAR(1000),
 duration INT NOT NULL,
 price DECIMAL(12,2) NOT NULL,
 status VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS itinerary (
 itinerary_id INT AUTO_INCREMENT PRIMARY KEY,
 package_id INT NOT NULL,
 day_number INT NOT NULL,
 destination VARCHAR(100),
 activities VARCHAR(1000),
 accommodation VARCHAR(255),
 transport VARCHAR(255),
 CONSTRAINT uq_package_day UNIQUE (package_id,day_number),
 CONSTRAINT fk_itinerary_package FOREIGN KEY (package_id)
 REFERENCES tour_package(package_id) ON DELETE CASCADE
);

INSERT INTO tour_package(package_name,description,duration,price,status)
VALUES ('7-Day Sri Lanka Cultural Tour',
'Colombo, Kandy, Nuwara Eliya and Ella cultural and scenic tour.',
7,250000.00,'AVAILABLE');

INSERT INTO itinerary(package_id,day_number,destination,activities,accommodation,transport)
SELECT package_id,1,'Colombo','Airport pickup and city sightseeing.','Colombo Hotel','Private Vehicle'
FROM tour_package WHERE package_name='7-Day Sri Lanka Cultural Tour' LIMIT 1;

INSERT INTO itinerary(package_id,day_number,destination,activities,accommodation,transport)
SELECT package_id,2,'Kandy','Temple visit and cultural show.','Kandy Hotel','Private Vehicle'
FROM tour_package WHERE package_name='7-Day Sri Lanka Cultural Tour' LIMIT 1;

