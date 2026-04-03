# Easy 2 Work – On-Demand Home Service Platform

<div align="center">

![Easy 2 Work Logo](web-ui/src/main/webapp/images/logo.png)

**India's Premier On-Demand Home Service Application**

Book trusted professionals for cleaning, repair, and maintenance services at your doorstep.

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.java.com)
[![Flutter](https://img.shields.io/badge/Flutter-3.0+-blue.svg)](https://flutter.dev)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org)

[Features](#-features) • [Tech Stack](#-technology-stack) • [Installation](#-installation--setup) • [Screenshots](#-screenshots) • [API Docs](#-api-documentation)

</div>

---

## 📖 About

**Easy 2 Work** is a comprehensive on-demand home service platform connecting users with verified professionals for household services including electrical repairs, AC servicing, laundry, and deep cleaning. Real-time tracking, secure payments, and quality service guaranteed.

**Service Area**: Varanasi | **Status**: Active Development | **Version**: 1.0.0

---

## ✨ Features

### For Users
- 🔍 **Service Discovery** - Browse 8+ service categories
- 🛒 **Easy Booking** - Simple 3-step process: Select → Cart → Pay
- 📍 **Address Management** - Save multiple delivery addresses
- 📱 **Real-time Tracking** - Live location and ETA of professionals
- 💳 **Multiple Payment Options** - Cash, UPI, Card, Net Banking
- ⭐ **Ratings & Reviews** - Rate and review service quality
- 🔔 **Push Notifications** - Real-time booking updates

### For Service Professionals
- 📋 **Job Management** - Receive and accept job requests
- 🗺️ **Navigation** - In-app navigation to customer location
- 💰 **Earnings Dashboard** - Track daily/weekly/monthly earnings
- 📅 **Schedule Management** - Manage availability and work schedule

### For Admin
- 👥 **User Management** - Manage users and professionals
- 🛠️ **Service Management** - Add/edit services and pricing
- 📊 **Analytics Dashboard** - Revenue and performance insights
- 🌍 **Area Management** - Expand to new cities

---

## 🛠 Technology Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Core programming language |
| Maven | 3.8+ | Build automation |
| Jakarta Servlet | 5.0+ | Web application framework |
| Jakarta Server Pages (JSP) | 3.0+ | Dynamic web pages |
| MySQL | 8.0+ | Primary database |
| JDBC | 4.3+ | Database connectivity |
| Jetty | 11.0+ | Development server |

### Frontend
- **HTML5, CSS3, JavaScript (ES6+)** - Core web technologies
- **Bootstrap** - Responsive UI framework
- **jQuery** - DOM manipulation and AJAX
- **Font Awesome** - Icon library

### Mobile App (Flutter)
| Package | Purpose |
|---------|---------|
| `firebase_core`, `firebase_auth` | Authentication |
| `cloud_firestore` | Cloud database |
| `firebase_messaging` | Push notifications |
| `google_maps_flutter` | Maps and location |
| `provider` / `riverpod` | State management |
| `razorpay_flutter` | Payment gateway |
| `dio` | HTTP client |
| `geolocator` | Location services |

### DevOps
- **Git/GitHub** - Version control
- **Docker** - Containerization
- **Jenkins/GitHub Actions** - CI/CD

---

## 📁 Project Structure

```
easy-2-work/
├── backend/
│   ├── catalog/              # Service catalog and labels
│   ├── core/                 # Domain models and business logic
│   └── service/              # Servlets, repositories, services
├── web-ui/                   # Jakarta JSP web application
│   └── src/main/webapp/
│       ├── index.jsp         # Landing page
│       ├── book.jsp          # Booking page
│       ├── track.jsp         # Tracking page
│       ├── css/              # Stylesheets
│       ├── js/               # JavaScript files
│       └── images/           # Static assets
├── app/                      # Flutter mobile app (optional)
│   ├── lib/
│   │   ├── screens/          # UI screens
│   │   ├── models/           # Data models
│   │   ├── services/         # API services
│   │   └── providers/        # State management
│   └── pubspec.yaml
├── docs/                     # Documentation
├── pom.xml                   # Root Maven POM
└── README.md
```

---

## 🔄 User Flow

```
Homepage → Browse Services → Add to Cart → Login/Signup →
Select Address → Choose Date & Time → Payment → Booking Confirmed →
Professional Assigned → Real-time Tracking → Service Completed →
Rate & Review
```

**Key Steps:**
1. **Discover** - Browse 8 service categories
2. **Select** - Add services to cart
3. **Checkout** - Login and enter address
4. **Schedule** - Choose date and time slot
5. **Pay** - Complete payment (Online/Cash)
6. **Track** - Live location tracking
7. **Complete** - Service completion and feedback

---

## 🚀 Installation & Setup

### Prerequisites
- Java 17+, Maven 3.8+, MySQL 8.0+, Git
- Flutter 3.0+ (for mobile app)

### Quick Start

**1. Clone Repository**
```bash
git clone https://github.com/yourusername/easy-2-work.git
cd easy-2-work
```

**2. Setup Database**
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

**3. Configure Database Connection**

Edit `backend/service/src/main/resources/db.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/easy2work
db.username=easy2work_user
db.password=password
```

**4. Build & Run**
```bash
# Build project
mvn clean install

# Run web application
mvn -pl web-ui jetty:run
```

Access at: **http://localhost:8080**

### Mobile App Setup

```bash
cd app
flutter create .
flutter pub get

# Configure Firebase (add google-services.json & GoogleService-Info.plist)
# Update API endpoint in lib/utils/constants.dart

flutter run
```

---

## 📚 API Documentation

### Base URL
```
Development: http://localhost:8080/api
Production: https://api.easy2work.com/api
```

### Authentication
```
Authorization: Bearer <jwt_token>
```

### Core Endpoints

**User Registration**
```http
POST /api/users/register
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
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Get All Services**
```http
GET /api/services
```

**Create Booking**
```http
POST /api/bookings
Authorization: Bearer <token>
{
  "serviceId": "1",
  "addressId": "1",
  "scheduledDate": "2025-04-05",
  "timeSlot": "10:00-12:00",
  "paymentMethod": "ONLINE"
}
```

**Track Booking**
```http
GET /api/bookings/{bookingId}/track
Authorization: Bearer <token>
```

**Initiate Payment**
```http
POST /api/payments/initiate
{
  "bookingId": "123",
  "amount": 500,
  "currency": "INR"
}
```

---

## 🏠 Services Offered

| Service | Description | Price | Duration |
|---------|-------------|-------|----------|
| ⚡ **Electrical Repair** | Wiring, switches, fault fixing | ₹199 | 30-60 min |
| ❄️ **AC Servicing** | Installation, repair, maintenance | ₹399 | 45-90 min |
| 🌀 **Cooler Repair** | Repair and servicing | ₹249 | 30-45 min |
| 🧺 **Laundry** | Washing, ironing, dry cleaning | ₹99 | 60-120 min |
| 🪟 **Window Cleaning** | Inside/outside glass cleaning | ₹149 | 30-45 min |
| 🍽️ **Utensils** | Utensil washing, kitchen cleanup | ₹99 | 30-60 min |
| 🏠 **Balcony Cleaning** | Sweeping, mopping, maintenance | ₹129 | 20-30 min |
| 🚿 **Bathroom Cleaning** | Deep cleaning, sanitization | ₹199 | 45-60 min |

**All services include**: Verified professionals, tools & equipment, quality assurance, on-time guarantee, secure payment

---

## 📸 Screenshots

<div align="center">

### Website

<table>
  <tr>
    <td><img src="docs/screenshots/web-homepage.png" width="300"/><br/><b>Homepage</b></td>
    <td><img src="docs/screenshots/web-booking.png" width="300"/><br/><b>Booking</b></td>
    <td><img src="docs/screenshots/web-tracking.png" width="300"/><br/><b>Tracking</b></td>
  </tr>
</table>

### Mobile App

<table>
  <tr>
    <td><img src="docs/screenshots/app-home.png" width="200"/><br/><b>All Services</b></td>
    <td><img src="docs/screenshots/app-cart.png" width="200"/><br/><b>Cart</b></td>
    <td><img src="docs/screenshots/app-tracking.png" width="200"/><br/><b>Live Tracking</b></td>
    <td><img src="docs/screenshots/app-profile.png" width="200"/><br/><b>Profile</b></td>
  </tr>
</table>

</div>

**Note**: Save your screenshots in `docs/screenshots/` directory and update paths above.

---

## 🎥 Video Demo

<div align="center">

### Website Demo
[![Website Demo](https://img.youtube.com/vi/YOUR_VIDEO_ID/maxresdefault.jpg)](https://www.youtube.com/watch?v=YOUR_VIDEO_ID)

### Mobile App Demo
[![App Demo](https://img.youtube.com/vi/YOUR_VIDEO_ID/maxresdefault.jpg)](https://www.youtube.com/watch?v=YOUR_VIDEO_ID)

**To add videos**: Upload to YouTube and replace `YOUR_VIDEO_ID` with actual video ID

</div>

---

## 📱 Mobile App Development Guide

### Core Screens
1. **Splash & Onboarding** - Brand intro and permissions
2. **Home** - Service category grid (8 tiles)
3. **Service Detail** - Description, pricing, reviews
4. **Cart** - Selected services with quantity
5. **Authentication** - Login/signup/OTP verification
6. **Address** - Add/edit/select delivery address
7. **Booking** - Date, time slot, instructions
8. **Payment** - Razorpay integration
9. **Tracking** - Live map with professional location
10. **History** - Past and upcoming bookings
11. **Profile** - User details and settings
12. **Rating** - Service feedback and reviews

### Development Checklist
- [ ] Setup Firebase & Razorpay
- [ ] Implement authentication module
- [ ] Build home & service screens
- [ ] Create booking flow
- [ ] Integrate payment gateway
- [ ] Add real-time tracking
- [ ] Implement push notifications
- [ ] Add profile management
- [ ] Testing & deployment

---

## 🗺️ Roadmap

**Phase 1: MVP** ✅
- Core booking, 8 services, payments, tracking

**Phase 2: Enhanced Features** 🚧
- Advanced search, offers, loyalty program, multi-language

**Phase 3: Scale** 📅
- 10+ cities, subscriptions, corporate bookings, analytics

**Phase 4: Innovation** 🔮
- IoT integration, video consultation, AR previews

---

## 🤝 Contributing

We welcome contributions!

**Steps:**
1. Fork the repository
2. Create feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -m "feat: add new feature"`
4. Push to branch: `git push origin feature/new-feature`
5. Open Pull Request

**Commit Convention**: `feat:`, `fix:`, `docs:`, `style:`, `refactor:`, `test:`, `chore:`

---

## 📄 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) file for details.

---

## 📞 Contact

- **Email**: support@easy2work.com
- **Phone**: +91 98765 43210
- **Location**: Varanasi, Uttar Pradesh
- **GitHub**: [GitHub Issues](https://github.com/yourusername/easy-2-work/issues)

### Social Media
[Facebook](https://facebook.com/easy2work) • [Twitter](https://twitter.com/easy2work) • [Instagram](https://instagram.com/easy2work.in) • [LinkedIn](https://linkedin.com/company/easy2work)

---

<div align="center">

**Built with ❤️ in India**

© 2025 Easy 2 Work – On-Demand Home Service Platform

*Your home, professionally serviced — exactly when you need it.*

![Status](https://img.shields.io/badge/status-active-success.svg)
![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Last Commit](https://img.shields.io/github/last-commit/yourusername/easy-2-work)

[⬆ Back to Top](#easy-2-work--on-demand-home-service-platform)

</div>
