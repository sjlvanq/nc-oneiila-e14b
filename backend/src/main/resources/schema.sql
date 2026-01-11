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
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id)
);

CREATE TABLE partners (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) UNIQUE NOT NULL
);

CREATE table charge_types(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE additional_charges (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  client_id BIGINT NOT NULL,
  charge_type_id BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  charge_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE attendance (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  client_id BIGINT NOT NULL,
  checked_in_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE group_activities (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE group_activity_attendance (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  client_id BIGINT NOT NULL,
  group_activity_id BIGINT NOT NULL,
  attendance_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clients (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  partner_id BIGINT, --Nullable
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(15),
  gender VARCHAR(10),
  age INT,
  near_location BOOLEAN,
  promo_friends BOOLEAN,
  registration_date DATE,
  contract_start_date DATE,
  contract_period INT,
  group_visit BOOLEAN,
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

-- Relación entre additional_charges y charge_types
ALTER TABLE additional_charges
ADD CONSTRAINT fk_additional_charges_charge_types
FOREIGN KEY (charge_type_id) REFERENCES charge_types(id);

-- Relación entre additional_charges y clients
ALTER TABLE additional_charges
ADD CONSTRAINT fk_additional_charges_clients
FOREIGN KEY (client_id) REFERENCES clients(id);

-- Relación entre attendance y clients
ALTER TABLE attendance
ADD CONSTRAINT fk_attendance_client
FOREIGN KEY (client_id) REFERENCES clients(id);

-- Relación entre group_activity_attendance y clients
ALTER TABLE group_activity_attendance
ADD CONSTRAINT fk_gaa_client
FOREIGN KEY (client_id) REFERENCES clients(id);

-- Relación entre group_activity_attendance y group_activities
ALTER TABLE group_activity_attendance
ADD CONSTRAINT fk_gaa_activity
FOREIGN KEY (group_activity_id) REFERENCES group_activities(id);
