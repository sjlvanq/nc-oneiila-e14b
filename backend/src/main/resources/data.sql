-- Clients
INSERT INTO clients (plan, fecha_alta) VALUES
('Basic', '2023-01-01'),
('Premium', '2023-03-15');

-- Users (password: 123456)
INSERT INTO users (email, password_hash, name, active, created_at) VALUES
('hola@demo.com', '$2a$12$hjMrF2t7GElG98KX0swmKOlw6p9cZcrmUQMrRJRUO6Cm.kYId3wBm', 'Hola User', TRUE, CURRENT_TIMESTAMP),
('admin@demo.com', '$2a$12$hjMrF2t7GElG98KX0swmKOlw6p9cZcrmUQMrRJRUO6Cm.kYId3wBm', 'Admin User', TRUE, CURRENT_TIMESTAMP),
('analyst@demo.com', '$2a$12$hjMrF2t7GElG98KX0swmKOlw6p9cZcrmUQMrRJRUO6Cm.kYId3wBm', 'Analyst User', TRUE, CURRENT_TIMESTAMP);

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
