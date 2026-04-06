# Authentication System Setup Guide

## Overview

I've implemented a complete authentication system for Easy2Work with:
- User login and registration
- Admin login portal
- Session-based authentication
- Protected routes with authentication filters
- Password hashing with SHA-256

## What's Included

### Backend Components

1. **User Model** (`backend/core/src/main/java/com/easy2work/core/model/User.java`)
   - User entity with fields: email, password, firstName, lastName, phone, role
   - Support for CUSTOMER and ADMIN roles

2. **Password Utility** (`backend/core/src/main/java/com/easy2work/core/security/PasswordUtil.java`)
   - Secure password hashing using SHA-256 with salt
   - Password verification

3. **UserRepository** (`backend/service/src/main/java/com/easy2work/backend/repository/UserRepository.java`)
   - Database operations for user management
   - Methods: findByEmail, findById, create, updateLastLogin

4. **Authentication Servlets**
   - `UserLoginServlet` - Handle user login
   - `UserRegisterServlet` - Handle user registration
   - `AdminLoginServlet` - Handle admin login
   - `LogoutServlet` - Handle logout
   - `AdminLogoutServlet` - Handle admin logout

5. **Authentication Filters**
   - `UserAuthFilter` - Protect user routes
   - `AdminAuthFilter` - Protect admin routes

### Frontend Components

1. **Login Pages**
   - `/login` - User login page
   - `/register` - User registration page
   - `/admin/login` - Admin login page (port 8081)

2. **Updated Header**
   - Shows "Login" button when not authenticated
   - Shows "Logout" button when authenticated

## Database Setup

### 1. Configure MySQL Connection

Set environment variables:
```bash
export E2W_JDBC_URL="jdbc:mysql://localhost:3306/easy2work"
export E2W_JDBC_USER="your_mysql_username"
export E2W_JDBC_PASSWORD="your_mysql_password"
```

Or configure in `web.xml` (user-portal and admin-portal):
```xml
<context-param>
  <param-name>jdbcUrl</param-name>
  <param-value>jdbc:mysql://localhost:3306/easy2work</param-value>
</context-param>
<context-param>
  <param-name>jdbcUser</param-name>
  <param-value>your_mysql_username</param-value>
</context-param>
<context-param>
  <param-name>jdbcPassword</param-name>
  <param-value>your_mysql_password</param-value>
</context-param>
```

### 2. Create Users Table

Run the SQL script: `docs/database/users_table.sql`

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL,
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- Default admin user
-- Email: admin@easy2work.com
-- Password: admin123
INSERT INTO users (email, password_hash, first_name, last_name, phone, role) VALUES
('admin@easy2work.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User', '1234567890', 'ADMIN');
```

## Testing the System

### User Portal (http://localhost:8080)

1. **Register a New Account**
   - Go to http://localhost:8080/register
   - Fill in the registration form
   - Submit to create your account
   - You'll be automatically logged in

2. **Login with Existing Account**
   - Go to http://localhost:8080/login
   - Enter email and password
   - Submit to login

3. **Protected Routes**
   - `/booking` - Requires login
   - `/my-bookings` - Requires login
   - If not logged in, you'll be redirected to login page

4. **Logout**
   - Click "Logout" in the navigation bar

### Admin Portal (http://localhost:8081)

1. **Default Admin Login**
   - Go to http://localhost:8081
   - Email: admin@easy2work.com
   - Password: admin123

2. **Protected Routes**
   - `/bookings` - Requires admin login
   - `/update-status` - Requires admin login

## Security Features

1. **Password Hashing**
   - All passwords are hashed using SHA-256 with random salt
   - Passwords are never stored in plain text

2. **Session Management**
   - User sessions are managed by servlet container
   - Session attributes store user information

3. **Role-Based Access**
   - Separate authentication for users and admins
   - Admin users cannot log in through user portal
   - Regular users cannot access admin portal

4. **Protected Routes**
   - Authentication filters protect sensitive routes
   - Automatic redirect to login for unauthenticated users

## Running Without Database

If you don't configure a database:
- Authentication features will be disabled
- The app will log warnings but continue to work
- You can still access public pages

## Next Steps

1. Set up MySQL and run the SQL scripts
2. Configure database connection
3. Restart the application
4. Test user registration and login
5. Test admin login
6. Try accessing protected routes

## URLs

- User Portal: http://localhost:8080
- User Login: http://localhost:8080/login
- User Register: http://localhost:8080/register
- Admin Portal: http://localhost:8081
- Admin Login: http://localhost:8081/login
