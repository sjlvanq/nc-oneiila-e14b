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
    id,
    client_name,
    gender,
    near_location,
    partner_employee,
    promo_friends,
    client_phone,
    contract_period,
    group_visit,
    age,
    avg_additional_charges_total,
    month_to_end_contract,
    lifetime,
    avg_class_frequency_total,
    avg_class_frequency_current_month,
    active
) VALUES 
-- Cliente 1: Perfil de retención alta (vives cerca, contrato largo)
(
    1, 'John Doe', 'MALE', 1, 1, 1, '555-0101', 12, 1, 32, 
    255.45, 12, 18, 3.5, 3.4, TRUE
),
-- Cliente 2: Perfil de alto riesgo de Churn (contrato mensual, baja frecuencia)
(
    2, 'Jane Smith', 'FEMALE', 0, 0, 0, '555-0202', 1, 0, 24, 
    12.30, 1, 1, 0.8, 0.1, TRUE
),
-- Cliente 3: Perfil intermedio
(
    3, 'Mike Ross', 'MALE', 1, 0, 1, '555-0303', 6, 1, 28, 
    102.00, 6, 2, 2.1, 2.0, TRUE
),
-- Cliente 4: Nuevo cliente para pruebas
(
    4, 'María García López', 'FEMALE', 1, 0, 1,
    '555-0404', 12, 1, 27, 255.45, 6, 18, 3.2, 4.1, TRUE
);