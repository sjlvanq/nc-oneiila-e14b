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

-- Partners
INSERT INTO partners (name) VALUES 
('Gym Corp International'),
('Wellness Solutions');

-- Charge_types
INSERT INTO charge_types (description) VALUES
('Massage Therapy'),
('Dietary supplements');

-- Clients
INSERT INTO clients (
    client_name,
    client_phone,
    gender,
    age,
    near_location,
    partner_id,
    promo_friends,
    registration_date,
    contract_start_date,
    contract_period,
    group_visit,
    avg_class_frequency_total,
    avg_class_frequency_current_month,
    active
) VALUES 
-- Cliente 1: Perfil de retención alta
(
    'John Doe', '555-0101', 'MALE', 32, 1, 1, 1,
    '2024-01-01', '2024-01-01', 12, 1, 3.5, 3.4, 1
),
-- Cliente 2: Perfil de alto riesgo de Churn
(
    'Jane Smith', '555-0202', 'FEMALE', 24, 0, NULL, 0, 
    '2025-12-01', '2025-12-01', 1, 0, 0.8, 0.1, 1
),
-- Cliente 3: Perfil intermedio
(
    'Mike Ross', '555-0303', 'MALE', 28, 1, 2, 1, 
    '2025-11-15', '2025-11-15', 6, 1, 2.1, 2.0, 1
),
-- Cliente 4: Nuevo cliente para pruebas
(
    'María García López', '555-0404', 'FEMALE', 27, 1, 1, 1, 
    '2024-06-01', '2024-06-01', 12, 1, 3.2, 4.1, 1
);

-- Additional_charges
INSERT INTO additional_charges (client_id, charge_type_id, amount, charge_date) VALUES
(1, 2, 45.00, '2025-01-05'),
(1, 2, 35.50, '2025-01-15'),
(1, 1, 60.00, '2025-01-20'),
(2, 2, 12.30, '2025-12-10'),
(3, 1, 55.00, '2025-11-20'),
(3, 1, 47.00, '2025-12-05'),
(4, 2, 80.00, '2024-07-10'),
(4, 1, 65.00, '2024-08-15'),
(4, 2, 40.00, '2024-09-01');
