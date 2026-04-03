# Easy 2 Work – On-Demand Home Service

**India's quick Home Service App** – Book trusted cleaning & repair help at your doorstep.

![Easy 2 Work](web-ui/src/main/webapp/images/logo.png)

---

## 📁 Project structure

```
easy2work/
├── backend/                → Java (Maven): APIs, domain, DB
│   ├── catalog/            # Service catalogue copy / labels
│   ├── core/               # Models, booking rules
│   └── service/            # Servlets, repos, JSON (artifact: easy2work-backend)
├── web-ui/                 → Jakarta JSP site + static assets (WAR / Jetty)
├── pom.xml                 # Root Maven reactor
├── README.md
└── .gitignore
```

- **Site**: From repo root, `mvn -pl web-ui jetty:run` (or `cd web-ui && mvn jetty:run`), then open http://localhost:8080/
- **App** (optional): From repo root run `cd app && flutter create .` (once, to add android/ios), then `flutter pub get && flutter run` if the `app/` folder exists.

---

## 📱 App बनाने के लिए यह Website Reference है

यह website **Easy 2 Work** app का reference है। App **Flutter** से बनेगा। इस website को follow करके mobile app बनाएं। नीचे app में क्या-क्या implement करना है, वो सब detail में दिया गया है।

---

## Overview

Easy 2 Work एक on-demand home service platform है। Users verified professionals से electrical repair, AC servicing, cooler repair, laundry, cleaning आदि book कर सकते हैं। अभी **Varanasi** में live है और और शहरों में expand हो रहा है।

---

## 🎯 App में बनाने वाले Main Screens (Website के हिसाब से)

Website पर जो phone mockups दिखते हैं, उनके अनुसार app में ये screens होने चाहिए:

### 1. All Services Screen
- Header: "Easy 2 Work"
- Label: "All services"
- Service tiles: Electrical ⚡, AC ❄️, Laundry 🧺, Window 🪟 (और बाकी services)
- User service select करके आगे बढ़े

### 2. Book Service / Cart Screen
- Header: "Book service"
- Selected services की cards (जैसे Electrical Repair, AC Servicing)
- हर card पर "Add to cart" / quantity
- Bottom पर "Proceed" button

### 3. Track Screen
- Header: "Track"
- Status: "Engineer on the way"
- Map: User का address
- ETA: "~15 mins"

---

## User Flow (How it works – Website के steps)

1. **Step 1** – Pick services (सभी services में से choose करो)
2. **Step 2** – Add to cart (cart में add करो)
3. **Step 3** – Pay and done (pay करो, फिर service complete)

---

## App में Implement करने वाली Features

| Feature | Description |
|---------|-------------|
| On-demand booking | User जब चाहे तब service book कर सके |
| Verified professionals | सभी engineers registered और verified हों |
| Service categories | Electrical, AC, Cooler, Laundry, Window, Utensils, Balcony, Bathroom |
| Cart & checkout | Multiple services add करके एक साथ book कर सके |
| Real-time tracking | Engineer की location और ETA track कर सके |
| Push notifications | Booking confirm, engineer on way, job done आदि |

---

## Services List (Website से – App में same रखें)

| Service | Description |
|--------|-------------|
| Electrical Repair | Wiring, fuse, switch और electrical fault fix |
| AC Servicing | AC installation, repair और maintenance |
| Cooler Repair | Cooler repair और servicing घर पर |
| Laundry | Washing, ironing और laundry help |
| Window Cleaning | अंदर-बाहर window cleaning |
| Utensils | बर्तन धोना और kitchen cleanup |
| Balcony Cleaning | Balcony sweep, mop और upkeep |
| Bathroom Cleaning | Bathroom deep clean और sanitisation |

---

## App Tech Stack – Flutter

App **Flutter** से बनेगा (Android + iOS दोनों के लिए).

| Use Case | Flutter Package |
|----------|-----------------|
| Auth, Database, Notifications | `firebase_core`, `firebase_auth`, `cloud_firestore`, `firebase_messaging` |
| Maps & Live Tracking | `google_maps_flutter` या `flutter_map` |
| State Management | `provider` या `riverpod` |
| Payment | `razorpay_flutter` |
| HTTP / API | `dio` या `http` |

---

## Website (web-ui/) – Reference

Landing and pages live in **`web-ui/src/main/webapp/`**: e.g. `index.jsp`, `book.jsp`, `terms.jsp`, `privacy.jsp`, `css/style.css`, `images/`. Use it as reference for the app UI and copy.

---

## App Development Checklist

- [ ] All Services screen – service list और selection
- [ ] Book Service / Cart screen – add to cart, proceed
- [ ] User registration & login
- [ ] Address selection (map / manual)
- [ ] Payment integration
- [ ] Track screen – engineer location, ETA
- [ ] Engineer app (अलग app) – job accept, navigation
- [ ] Push notifications
- [ ] Terms & Privacy screens

---

## Contact

- **Website**: Run Jetty from `web-ui` (see above) or build the WAR and deploy to your servlet container.
- **Service area**: Varanasi (और शहरों में expand हो रहा है)

---

© 2025 Easy 2 Work – On-Demand Home Service

*Your home, professionally cleaned — exactly when you need it.*
