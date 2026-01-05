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

CREATE TABLE user_roles (
  user_id BIGINT,
  role_id BIGINT,
  PRIMARY KEY (user_id, role_id)
);

CREATE TABLE clients (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  client_name VARCHAR(100),
  gender VARCHAR(10),
  near_location INT,
  partner_employee INT,
  promo_friends INT,
  client_phone VARCHAR(15),
  contract_period INT,
  group_visit INT,
  age INT,
  avg_additional_charges_total DECIMAL(10,2),
  month_to_end_contract INT,
  lifetime INT,
  avg_class_frequency_total DECIMAL(10,2),
  avg_class_frequency_current_month DECIMAL(10,2),
  active BOOLEAN DEFAULT TRUE
);

-- Relaciones user_roles
ALTER TABLE user_roles
ADD CONSTRAINT fk_user_roles_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE user_roles
ADD CONSTRAINT fk_user_roles_role
FOREIGN KEY (role_id) REFERENCES roles(id);
