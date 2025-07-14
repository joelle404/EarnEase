CREATE TABLE IF NOT EXISTS staff (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    password VARCHAR(100)
    
);

CREATE TABLE IF NOT EXISTS services (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    base_price NUMERIC(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    client_id INT REFERENCES clients(id),
    staff_id INT REFERENCES staff(id),
    service_id INT REFERENCES services(id),
    amount_paid NUMERIC(10,2),
    percentage_given NUMERIC(5,2),
    percentage_recipient_id INT REFERENCES staff(id),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS rents (
    id SERIAL PRIMARY KEY,
    staff_id INT REFERENCES staff(id),
    month VARCHAR(7), -- e.g., '2025-07'
    amount NUMERIC(10,2),
    paid_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (staff_id, month) -- prevents duplicate rent for same staff & month
);
CREATE TABLE IF NOT EXISTS product_purchases (
    id SERIAL PRIMARY KEY,
    staff_id INT REFERENCES staff(id),
    product_name VARCHAR,
    amount_spent NUMERIC(10,2),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS staff_payments (
    id SERIAL PRIMARY KEY,
    payer_id INT REFERENCES staff(id),        -- e.g., Tamer
    recipient_id INT REFERENCES staff(id),    -- e.g., Celina
    amount NUMERIC(10,2),
    reason TEXT,                              -- e.g., 'Hair assist', 'Cleanup help'
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
