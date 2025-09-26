CREATE DATABASE university_reminder;
USE university_reminder;

-- Company table
CREATE TABLE company (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- University table
CREATE TABLE university (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    address TEXT,
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, DELETED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (company_id) REFERENCES company(id)
);

-- User table
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    university_id BIGINT, -- NULL for company admin
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Plain text for this sample
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role VARCHAR(20) NOT NULL, -- COMPANY_ADMIN, UNIVERSITY_ADMIN, EVENT_MANAGER, STUDENT, PARENT
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, DELETED
    parent_id BIGINT, -- For linking students to parents
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (parent_id) REFERENCES user(id)
);

-- Event table
CREATE TABLE event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    university_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    venue VARCHAR(255),
    starts_at DATETIME NOT NULL,
    ends_at DATETIME NOT NULL,
    visibility VARCHAR(20) DEFAULT 'PUBLIC', -- PUBLIC, PRIVATE
    status VARCHAR(20) DEFAULT 'SCHEDULED', -- SCHEDULED, CANCELLED, COMPLETED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (created_by) REFERENCES user(id),
    FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- Bill table
CREATE TABLE bill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    university_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    parent_id BIGINT, -- Optional
    amount DECIMAL(10, 2) NOT NULL,
    description TEXT,
    due_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, ACCEPTED, PAID, REJECTED
    editable_until DATETIME, -- Created_at + 48 hours
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (student_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES user(id),
    FOREIGN KEY (created_by) REFERENCES user(id),
    FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- Bill Notice table
CREATE TABLE bill_notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    university_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    valid_from DATE NOT NULL,
    valid_to DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (created_by) REFERENCES user(id),
    FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- Review table
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    university_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (event_id) REFERENCES event(id),
    FOREIGN KEY (student_id) REFERENCES user(id),
    FOREIGN KEY (created_by) REFERENCES user(id),
    FOREIGN KEY (updated_by) REFERENCES user(id)
);

-- Notification table
CREATE TABLE notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    university_id BIGINT,
    user_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL, -- BILL, EVENT, SYSTEM
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    read_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Delivery Log table
CREATE TABLE delivery_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    notification_id BIGINT NOT NULL,
    channel VARCHAR(20) NOT NULL, -- EMAIL, IN_APP
    status VARCHAR(20) NOT NULL, -- SENT, FAILED, PENDING
    error TEXT,
    sent_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (notification_id) REFERENCES notification(id)
);

-- Email history table
CREATE TABLE email_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    university_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    recipient_id BIGINT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    sent_at DATETIME NOT NULL,
    updated_at DATETIME,
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (sender_id) REFERENCES user(id),
    FOREIGN KEY (recipient_id) REFERENCES user(id)
);

-- Insert default company for testing
INSERT INTO company (id, name, description, status) 
VALUES (1, 'University Management System', 'Global company managing universities', 'ACTIVE');

-- Insert company admin user (predefined credentials with plain text password)
INSERT INTO user (id, university_id, email, password, first_name, last_name, role, status) 
VALUES (1, NULL, 'admin@company.com', 'admin123', 'Company', 'Admin', 'COMPANY_ADMIN', 'ACTIVE');

-- Add foreign key constraints for created_by and updated_by after user table is populated
ALTER TABLE company ADD CONSTRAINT fk_company_created_by FOREIGN KEY (created_by) REFERENCES user(id);
ALTER TABLE company ADD CONSTRAINT fk_company_updated_by FOREIGN KEY (updated_by) REFERENCES user(id);

-- Update company with created_by
UPDATE company SET created_by = 1, updated_by = 1 WHERE id = 1;

-- Create indexes for performance
CREATE INDEX idx_university_company ON university(company_id);
CREATE INDEX idx_user_university ON user(university_id);
CREATE INDEX idx_user_email ON user(email);
CREATE INDEX idx_bill_student ON bill(student_id);
CREATE INDEX idx_bill_university ON bill(university_id);
CREATE INDEX idx_event_university ON event(university_id);
CREATE INDEX idx_review_event ON review(event_id);
CREATE INDEX idx_review_student ON review(student_id);
CREATE INDEX idx_notification_user ON notification(user_id);
