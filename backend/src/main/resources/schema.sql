CREATE TABLE clients (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  plan VARCHAR(50) NOT NULL,
  fecha_alta DATE
);

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  name VARCHAR(100),
  active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE permissions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
  user_id BIGINT,
  role_id BIGINT,
  PRIMARY KEY (user_id, role_id)
);

CREATE TABLE role_permissions (
  role_id BIGINT,
  permission_id BIGINT,
  PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE user_client_access (
  user_id BIGINT,
  client_id BIGINT,
  access_level VARCHAR(20),
  PRIMARY KEY (user_id, client_id)
);
-- Relaciones user_roles
ALTER TABLE user_roles
ADD CONSTRAINT fk_user_roles_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE user_roles
ADD CONSTRAINT fk_user_roles_role
FOREIGN KEY (role_id) REFERENCES roles(id);

-- Relaciones role_permissions
ALTER TABLE role_permissions
ADD CONSTRAINT fk_role_permissions_role
FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE role_permissions
ADD CONSTRAINT fk_role_permissions_permission
FOREIGN KEY (permission_id) REFERENCES permissions(id);

-- Relaciones user_client_access
ALTER TABLE user_client_access
ADD CONSTRAINT fk_user_client_access_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE user_client_access
ADD CONSTRAINT fk_user_client_access_client
FOREIGN KEY (client_id) REFERENCES clients(id);
