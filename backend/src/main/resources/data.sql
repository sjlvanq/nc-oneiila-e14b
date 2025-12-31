-- Users (password: 123456)
INSERT INTO users (email, password_hash, name, active, created_at) VALUES
('hola@demo.com', '$2a$12$hjMrF2t7GElG98KX0swmKOlw6p9cZcrmUQMrRJRUO6Cm.kYId3wBm', 'Hola User', TRUE, CURRENT_TIMESTAMP),
('admin@demo.com', '$2a$12$hjMrF2t7GElG98KX0swmKOlw6p9cZcrmUQMrRJRUO6Cm.kYId3wBm', 'Admin User', TRUE, CURRENT_TIMESTAMP),
('analyst@demo.com', '$2a$12$hjMrF2t7GElG98KX0swmKOlw6p9cZcrmUQMrRJRUO6Cm.kYId3wBm', 'Analyst User', TRUE, CURRENT_TIMESTAMP);

-- Roles
INSERT INTO roles (name) VALUES
('ADMIN'),
('ANALYST');

-- User → Roles
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- Hola User → ADMIN
(2, 1), -- Admin User → ADMIN
(3, 2); -- Analyst User → ANALYST

-- Clients

INSERT INTO clients (
    client_name,
    gender,
    near_location,
    partner_employee,
    promo_friends,
    client_phone,
    age,
    contract_period,
    month_to_end_contract,
    lifetime_months,
    avg_class_frequency_total,
    avg_class_frequency_current_month,
    churn
) VALUES
-- Loyal client
('John Doe', 'MALE', 1, 1, 1, '5551234567', 32, 12, 10, 8, 3.50, 3.60, 0),

-- High risk client
('Jane Smith', 'FEMALE', 0, 0, 0, '5559876543', 24, 1, 1, 1, 1.20, 0.20, 1),

-- Stable senior client
('Robert Brown', 'MALE', 1, 0, 0, '5554443332', 45, 6, 3, 12, 2.10, 2.00, 0),

-- Partner employee
('Alice Johnson', 'FEMALE', 1, 1, 0, '5551112223', 29, 12, 11, 2, 2.80, 2.90, 0),

-- Disengaged client
('Michael Wilson', 'MALE', 0, 0, 1, '5556667778', 30, 1, 1, 3, 1.50, 0.00, 1);