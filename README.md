# Easy 2 Work – On-Demand Home Service Platform

<div align="center">

![Easy 2 Work Logo](web-ui/src/main/webapp/images/logo.png)

**India's Premier On-Demand Home Service Web Platform**

Book trusted professionals for cleaning, repair, and maintenance services at your doorstep.

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.java.com)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue.svg)](https://jakarta.ee)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com)

</div>

---

## 📖 About

**Easy 2 Work** is a web-based on-demand home service platform connecting users with verified professionals for household services including electrical repairs, AC servicing, laundry, and deep cleaning. Built with Java/Jakarta EE, it offers real-time booking management, secure payments, and service tracking.

**Service Area**: Varanasi | **Status**: Active Development | **Version**: 1.0.0

---

## ✨ Features

**For Customers:**
- Browse 8+ service categories
- Easy 3-step booking (Select → Cart → Pay)
- Multiple payment options (Cash, UPI, Card, Net Banking)
- Booking history and tracking
- Ratings & reviews

**For Service Professionals:**
- Job management and earnings tracking
- Customer details and schedule management

**For Admin:**
- User and service management
- Booking management and analytics

---

## 🛠 Technology Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17+ | Core programming language |
| **Maven** | 3.8+ | Build automation |
| **Jakarta Servlet** | 5.0+ | Web application framework |
| **Jakarta Server Pages (JSP)** | 3.0+ | Server-side page generation |
| **JDBC** | 4.3+ | Database connectivity |
| **MySQL** | 8.0+ | Relational database |
| **Jackson/JSON-P** | 2.0+ | JSON processing |
| **Jetty** | 11.0+ | Development server |

### Frontend
| Technology | Purpose |
|------------|---------|
| **HTML5** | Markup and structure |
| **CSS3** | Styling and responsive design |
| **JavaScript (ES6+)** | Client-side logic |
| **Bootstrap 5** | UI framework |
| **jQuery** | DOM manipulation |
| **Font Awesome** | Icons |

---

## 📁 Project Structure

```
easy-2-work/
├── backend/
│   ├── catalog/              # Service catalog
│   ├── core/                 # Domain models
│   │   └── src/main/java/com/easy2work/model/
│   └── service/              # Servlets & repositories
│       └── src/main/java/com/easy2work/
│           ├── servlet/      # HTTP servlets
│           ├── repository/   # Data access layer
│           └── service/      # Business services
├── web-ui/
│   └── src/main/webapp/
│       ├── index.jsp         # Landing page
│       ├── book.jsp          # Booking page
│       ├── track.jsp         # Tracking page
│       ├── login.jsp         # Login page
│       ├── signup.jsp        # Registration
│       ├── profile.jsp       # User profile
│       ├── history.jsp       # Booking history
│       ├── terms.jsp         # Terms & conditions
│       ├── privacy.jsp       # Privacy policy
│       ├── css/              # Stylesheets
│       ├── js/               # JavaScript files
│       └── images/           # Static assets
├── docs/
├── pom.xml                   # Root Maven POM
└── README.md
```

---

## 🌐 Web Application Pages

| Page | File | Description |
|------|------|-------------|
| **Homepage** | `index.jsp` | Landing page with services |
| **Services** | `services.jsp` | Service catalog |
| **Booking/Cart** | `book.jsp` | Shopping cart |
| **Login** | `login.jsp` | User authentication |
| **Signup** | `signup.jsp` | Registration |
| **Checkout** | `checkout.jsp` | Address and payment |
| **Order Tracking** | `track.jsp` | Real-time tracking |
| **User Profile** | `profile.jsp` | Account settings |
| **Booking History** | `history.jsp` | Past bookings |
| **Admin Dashboard** | `admin/dashboard.jsp` | Admin panel |

---

## 🔄 User Flow

**Complete User Journey:**
```
Homepage → Browse Services → Add to Cart → Login/Signup →
Select Address → Choose Date & Time → Payment → Booking Confirmed →
Professional Assigned → Order Tracking → Service Completed → Rate & Review
```

### Detailed Flow

**Step 1: Service Discovery** (`index.jsp`, `services.jsp`)
- User lands on homepage
- Browses 8 service categories
- Views service details and pricing
- Clicks on desired service

**Step 3: Authentication** (`login.jsp`, `signup.jsp`)
- New user: Registration with email/phone
- Existing user: Login with credentials
- Session management and authentication

**Step 4: Checkout** (`checkout.jsp`)
- Select or add delivery address
- Choose preferred service date
- Select available time slot
- Add special instructions (optional)

**Step 5: Payment** (`checkout.jsp`)
- Choose payment method:
  - Online Payment (Card/UPI/Net Banking via Razorpay)
  - Cash on Service
- Complete payment processing
- Generate booking confirmation

**Step 7: Tracking** (`track.jsp`)
- View assigned professional details
- Real-time status updates
- Track service progress
- Contact professional if needed
- Service completion and final payment

---

## 🚀 Installation & Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Git

### Quick Start

**Step 1: Clone Repository**
```bash
git clone https://github.com/yourusername/easy-2-work.git
cd easy-2-work
```

**Step 2: Setup Database**
```bash
mysql -u root -p
CREATE DATABASE easy2work;
CREATE USER 'easy2work_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON easy2work.* TO 'easy2work_user'@'localhost';
FLUSH PRIVILEGES;
exit;

# Import schema
mysql -u root -p easy2work < docs/database/schema.sql
```

**Step 3: Configure Database**

Edit `backend/service/src/main/resources/db.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/easy2work
db.username=easy2work_user
db.password=password
db.driver=com.mysql.cj.jdbc.Driver
```

**Step 4: Build Project**
```bash
mvn clean install
```

**Step 5: Run Application**
```bash
# Development (Jetty)
mvn -pl web-ui jetty:run

# Production (Tomcat)
mvn clean package
cp web-ui/target/easy2work-web.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh
```

**Step 6: Access Application**
- Development: http://localhost:8080
- Production: http://localhost:8080/easy2work-web

---

## 📚 API Endpoints

### Base URL
```
Development: http://localhost:8080/api
Production: https://api.easy2work.com/api
```

### Authentication
```
Authorization: Bearer <jwt_token>
```

### User Management

**Register User**
```http
POST /api/users/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+919876543210",
  "password": "password123"
}
```

**Login**
```http
POST /api/users/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  }
}
```

**Get User Profile**
```http
GET /api/users/profile
Authorization: Bearer <token>

Response:
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+919876543210",
  "addresses": [...]
}
```

### Service Management

**Get All Services**
```http
GET /api/services

Response:
[
  {
    "id": 1,
    "name": "Electrical Repair",
    "description": "Wiring, switches, fault fixing",
    "price": 199,
    "duration": "30-60 mins",
    "category": "ELECTRICAL"
  },
  ...
]
```

**Get Service by ID**
```http
GET /api/services/{serviceId}

Response:
{
  "id": 1,
  "name": "Electrical Repair",
  "description": "Wiring, switches, fault fixing",
  "price": 199,
  "duration": "30-60 mins",
  "category": "ELECTRICAL",
  "reviews": [...]
}
```

**Get Services by Category**
```http
GET /api/services/category/{categoryName}

Response:
[
  {
    "id": 1,
    "name": "Electrical Repair",
    "price": 199
  },
  ...
]
```

### Booking Management

**Create Booking**
```http
POST /api/bookings
Authorization: Bearer <token>
Content-Type: application/json

{
  "serviceId": 1,
  "addressId": 1,
  "scheduledDate": "2025-04-05",
  "timeSlot": "10:00-12:00",
  "specialInstructions": "Please call before arriving",
  "paymentMethod": "ONLINE"
}

Response:
{
  "bookingId": "BK123456",
  "status": "CONFIRMED",
  "serviceName": "Electrical Repair",
  "scheduledDate": "2025-04-05",
  "timeSlot": "10:00-12:00",
  "totalAmount": 199
}
```

**Get User Bookings**
```http
GET /api/bookings/user
Authorization: Bearer <token>

Response:
[
  {
    "bookingId": "BK123456",
    "serviceName": "Electrical Repair",
    "status": "CONFIRMED",
    "scheduledDate": "2025-04-05",
    "totalAmount": 199
  },
  ...
]
```

**Get Booking Details**
```http
GET /api/bookings/{bookingId}
Authorization: Bearer <token>

Response:
{
  "bookingId": "BK123456",
  "serviceName": "Electrical Repair",
  "status": "IN_PROGRESS",
  "scheduledDate": "2025-04-05",
  "timeSlot": "10:00-12:00",
  "address": {...},
  "professional": {
    "id": 10,
    "name": "Ramesh Kumar",
    "phone": "+919876543210",
    "rating": 4.5
  },
  "totalAmount": 199
}
```

**Track Booking**
```http
GET /api/bookings/{bookingId}/track
Authorization: Bearer <token>

Response:
{
  "bookingId": "BK123456",
  "status": "IN_PROGRESS",
  "professionalLocation": {
    "latitude": 25.3176,
    "longitude": 82.9739
  },
  "estimatedArrival": "15 mins",
  "statusUpdates": [
    {
      "status": "CONFIRMED",
      "timestamp": "2025-04-05T09:00:00Z"
    },
    {
      "status": "PROFESSIONAL_ASSIGNED",
      "timestamp": "2025-04-05T09:30:00Z"
    },
    {
      "status": "IN_PROGRESS",
      "timestamp": "2025-04-05T10:00:00Z"
    }
  ]
}
```

**Cancel Booking**
```http
POST /api/bookings/{bookingId}/cancel
Authorization: Bearer <token>
Content-Type: application/json

{
  "reason": "Change of plans"
}

Response:
{
  "bookingId": "BK123456",
  "status": "CANCELLED",
  "refundAmount": 199,
  "refundStatus": "PROCESSING"
}
```

### Payment Management

**Initiate Payment**
```http
POST /api/payments/initiate
Authorization: Bearer <token>
Content-Type: application/json

{
  "bookingId": "BK123456",
  "amount": 199,
  "currency": "INR"
}

Response:
{
  "orderId": "order_123abc",
  "amount": 199,
  "currency": "INR",
  "razorpayKey": "rzp_test_xxxxx"
}
```

**Verify Payment**
```http
POST /api/payments/verify
Authorization: Bearer <token>
Content-Type: application/json

{
  "razorpay_order_id": "order_123abc",
  "razorpay_payment_id": "pay_xyz789",
  "razorpay_signature": "signature_hash"
}

Response:
{
  "paymentId": "PAY123456",
  "status": "SUCCESS",
  "bookingId": "BK123456",
  "amount": 199,
  "transactionId": "TXN123456789"
}
```

**Get Payment History**
```http
GET /api/payments/history
Authorization: Bearer <token>

Response:
[
  {
    "paymentId": "PAY123456",
    "bookingId": "BK123456",
    "amount": 199,
    "status": "SUCCESS",
    "paymentMethod": "ONLINE",
    "transactionDate": "2025-04-05T09:00:00Z"
  },
  ...
]
```

### Address Management

**Add Address**
```http
POST /api/addresses
Authorization: Bearer <token>
Content-Type: application/json

{
  "addressLine1": "123 Main Street",
  "addressLine2": "Near City Mall",
  "city": "Varanasi",
  "state": "Uttar Pradesh",
  "pincode": "221001",
  "phone": "+919876543210",
  "isDefault": true
}

Response:
{
  "addressId": 1,
  "addressLine1": "123 Main Street",
  "city": "Varanasi",
  "pincode": "221001"
}
```

**Get User Addresses**
```http
GET /api/addresses
Authorization: Bearer <token>

Response:
[
  {
    "addressId": 1,
    "addressLine1": "123 Main Street",
    "city": "Varanasi",
    "pincode": "221001",
    "isDefault": true
  },
  ...
]
```

**Update Address**
```http
PUT /api/addresses/{addressId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "addressLine1": "456 New Street",
  "city": "Varanasi",
  "pincode": "221001"
}
```

**Delete Address**
```http
DELETE /api/addresses/{addressId}
Authorization: Bearer <token>

Response:
{
  "message": "Address deleted successfully"
}
```

### Review & Rating

**Add Review**
```http
POST /api/reviews
Authorization: Bearer <token>
Content-Type: application/json

{
  "bookingId": "BK123456",
  "rating": 5,
  "comment": "Excellent service! Very professional."
}

Response:
{
  "reviewId": 1,
  "bookingId": "BK123456",
  "rating": 5,
  "comment": "Excellent service! Very professional.",
  "createdAt": "2025-04-05T14:00:00Z"
}
```

**Get Service Reviews**
```http
GET /api/services/{serviceId}/reviews

Response:
[
  {
    "reviewId": 1,
    "userName": "John Doe",
    "rating": 5,
    "comment": "Excellent service!",
    "createdAt": "2025-04-05T14:00:00Z"
  },
  ...
]
```

---

## 📸 Screenshots

<div align="center">

<table>
  <tr>
    <td align="center">
      <img src="docs/screenshots/homepage.png" width="350"/><br/>
      <b>Homepage</b>
    </td>
    <td align="center">
      <img src="docs/screenshots/booking.png" width="350"/><br/>
      <b>Booking Page</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="docs/screenshots/checkout.png" width="350"/><br/>
      <b>Checkout</b>
    </td>
    <td align="center">
      <img src="docs/screenshots/tracking.png" width="350"/><br/>
      <b>Order Tracking</b>
    </td>
  </tr>
</table>

</div>

---

## 🎥 Video Demo

<div align="center">

[![Easy 2 Work Demo](https://img.youtube.com/vi/YOUR_VIDEO_ID/maxresdefault.jpg)](https://www.youtube.com/watch?v=YOUR_VIDEO_ID)

</div>

---

<div align="center">

**Built with ❤️ in India**

© 2025 Easy 2 Work – On-Demand Home Service Platform

*Your home, professionally serviced — exactly when you need it.*

![Status](https://img.shields.io/badge/status-active-success.svg)
![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)

**Tech Stack:** Java 17+ • Jakarta EE • JSP • MySQL • Bootstrap • JavaScript • Maven

</div>
