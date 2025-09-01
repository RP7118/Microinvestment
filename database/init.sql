-- Database initialization script for Micro-Investment Engine
-- Run this script after creating the database

-- Create database (run this separately)
-- CREATE DATABASE micro_investment;

-- Connect to the database and run the following:

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    savings_balance DECIMAL(19,2) DEFAULT 0.00,
    investment_balance DECIMAL(19,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    original_amount DECIMAL(19,2) NOT NULL,
    charged_amount DECIMAL(19,2) NOT NULL,
    round_up_amount DECIMAL(19,2) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Savings goals table
CREATE TABLE IF NOT EXISTS savings_goals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    goal_name VARCHAR(100) NOT NULL,
    description TEXT,
    target_amount DECIMAL(19,2) NOT NULL,
    current_amount DECIMAL(19,2) DEFAULT 0.00,
    target_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(transaction_date);
CREATE INDEX IF NOT EXISTS idx_savings_goals_user_id ON savings_goals(user_id);

-- Insert sample data (optional)
INSERT INTO users (username, email, password, first_name, last_name) VALUES
('demo_user', 'demo@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Demo', 'User');

-- Note: The password above is 'password' encrypted with BCrypt
-- In production, always use proper password hashing

-- Sample transaction
INSERT INTO transactions (user_id, original_amount, charged_amount, round_up_amount, description, category) VALUES
(1, 93.50, 94.00, 0.50, 'Coffee purchase', 'Food & Beverages');

-- Sample savings goal
INSERT INTO savings_goals (user_id, goal_name, description, target_amount, target_date) VALUES
(1, 'Vacation Fund', 'Save for summer vacation', 5000.00, '2024-06-01 00:00:00');

-- Update user's savings balance
UPDATE users SET savings_balance = 0.50 WHERE id = 1; 