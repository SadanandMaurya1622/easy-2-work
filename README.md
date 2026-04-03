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

## 📚 API Endpoints (used by the web UI)

Paths are relative to the deployed servlet context (for example `/easy2work-web`). Only method and path are listed here—no request/response bodies.

| Method | Path |
|--------|------|
| `GET` | `/api/stats` |
| `GET` | `/api/health` |
| `GET` | `/api/services` |
| `GET` | `/api/service-detail` |
| `GET` | `/api/bookings` |
| `POST` | `/api/booking` |

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
