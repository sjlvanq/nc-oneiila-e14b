-- ==========================================================
-- USERS
-- ==========================================================

-- Password for all users: 123456
INSERT INTO users (email, password_hash, name, active) VALUES
('admin@churninsight.com',
 '$2a$10$7qE9Z4zEw9nQ8q5B8oO3ru5xkR5n8k8YdE8C3n5Jp9s5yFJz5uHPe',
 'System Admin',
 TRUE),
('analyst@churninsight.com',
 '$2a$10$7qE9Z4zEw9nQ8q5B8oO3ru5xkR5n8k8YdE8C3n5Jp9s5yFJz5uHPe',
 'Senior Analyst',
 TRUE);

-- ==========================================================
-- ROLES
-- ==========================================================

INSERT INTO roles (name) VALUES
('ADMIN'),
('ANALYST');

INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1),
(2, 2);

-- ==========================================================
-- CLIENTS (GYM CHURN DATASET)
-- ==========================================================

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
('John Doe', 'Male', 1, 1, 1, '5551234567', 32, 12, 10, 8, 3.50, 3.60, 0),

-- High risk client
('Jane Smith', 'Female', 0, 0, 0, '5559876543', 24, 1, 1, 1, 1.20, 0.20, 1),

-- Stable senior client
('Robert Brown', 'Male', 1, 0, 0, '5554443332', 45, 6, 3, 12, 2.10, 2.00, 0),

-- Partner employee
('Alice Johnson', 'Female', 1, 1, 0, '5551112223', 29, 12, 11, 2, 2.80, 2.90, 0),

-- Disengaged client
('Michael Wilson', 'Male', 0, 0, 1, '5556667778', 30, 1, 1, 3, 1.50, 0.00, 1);
