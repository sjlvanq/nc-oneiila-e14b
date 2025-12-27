-- Clients
INSERT INTO clients (plan, fecha_alta) VALUES
('Basic', '2023-01-01'),
('Premium', '2023-03-15');

-- Users (password: 123456)
INSERT INTO users (email, password_hash, name, active, created_at) VALUES
('admin@demo.com', '$2a$10$7qE9Z4zEw9nQ8q5B8oO3ru5xkR5n8k8YdE8C3n5Jp9s5yFJz5uHPe', 'Admin User', TRUE, CURRENT_TIMESTAMP),
('analyst@demo.com', '$2a$10$7qE9Z4zEw9nQ8q5B8oO3ru5xkR5n8k8YdE8C3n5Jp9s5yFJz5uHPe', 'Analyst User', TRUE, CURRENT_TIMESTAMP);

-- Roles
INSERT INTO roles (name) VALUES
('ADMIN'),
('ANALYST');

-- Permissions
INSERT INTO permissions (name) VALUES
('READ_CLIENTS'),
('WRITE_CLIENTS');

-- User ↔ Client Access
INSERT INTO user_client_access (user_id, client_id, access_level) VALUES
(1, 1, 'FULL'),
(2, 1, 'READ');

-- User → Roles
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- Admin User → ADMIN
(2, 2); -- Analyst User → ANALYST
