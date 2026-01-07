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

CREATE TABLE partners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE clients (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  client_name VARCHAR(100),
  client_phone VARCHAR(15),
  gender VARCHAR(10),
  age INT,
  near_location BOOLEAN,
  partner_id BIGINT,
  promo_friends BOOLEAN,
  created_at DATE,
  contract_start_date DATE,
  contract_period INT,
  group_visit BOOLEAN,
  avg_additional_charges_total DECIMAL(10,2),
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

-- Relación entre clients y partners
ALTER TABLE clients
ADD CONSTRAINT fk_clients_partner
FOREIGN KEY (partner_id) REFERENCES partners(id);
