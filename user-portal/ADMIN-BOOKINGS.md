# Admin: kaise bookings dekhein

Is document mein likha hai ke **admin** Easy 2 Work par **kaun si booking kab, kis service ki, kis customer ne, kis address par** kaise dekh sakta hai.

---

## 1. Web admin page (recommended)

### Do browser mein test (local, bina MySQL)

Jab **`E2W_JDBC_URL` set nahi** (Jetty local demo), bookings **server ki memory** mein rehti hain. Is mode mein admin ka **fixed demo key** auto-chal jata hai:

| Demo admin key | `easy2work-local-dev` |
|----------------|------------------------|

1. **Browser A (admin):** `http://localhost:8080/admin/bookings?key=easy2work-local-dev` (port apna Jetty port.)
2. **Browser B (user):** `http://localhost:8080/book.jsp` se booking submit karein.
3. **Browser A** par page **Refresh** — nayi row table mein dikhegi.

**Yaad:** Ye key **sirf tab** use hoti hai jab **MySQL connect nahi** hai. Production (MySQL on) par is key se admin **404** rahega jab tak `E2W_ADMIN_KEY` (ya `easy2workAdminKey` in `web.xml`) set na ho.

### Production / MySQL — secret set karein

Jab database laga ho, demo key **band** ho jati hai. Tab zaroor environment variable:

| Variable | Value |
|----------|--------|
| `E2W_ADMIN_KEY` | Lamba random string (example: `openssl rand -hex 32`) |

**Optional:** `web.xml` context-param `easy2workAdminKey` (agar env use nahi karna).

Agar MySQL **on** hai aur **na** env **na** param → admin **404** (security).

### URL kholna

Browser mein (apna domain aur secret use karein):

```text
https://YOUR_DOMAIN/admin/bookings?key=YOUR_SECRET
```

Local Jetty + MySQL + apni key:

```text
http://localhost:8080/admin/bookings?key=YOUR_SECRET
```

Local Jetty **bina MySQL** (demo key):

```text
http://localhost:8080/admin/bookings?key=easy2work-local-dev
```

### Header se (production behtar)

Query string kabhi-kabhi **access logs** mein save ho jati hai. Production mein ye use karein:

- Header name: `X-Admin-Key`
- Header value: wahi jo `E2W_ADMIN_KEY` mein hai  

Example (curl):

```bash
curl -s -H "X-Admin-Key: YOUR_SECRET" "https://YOUR_DOMAIN/admin/bookings"
```

### Page par kya dikhega

Table columns roughly:

| Column | Matlab |
|--------|--------|
| **Ref** | Booking ID |
| **Booked at** | Booking create hone ka date/time (server timezone) |
| **Customer** | Naam |
| **Phone / Email** | Contact |
| **Service** | Service ka naam + code (e.g. AC, ELECTRICAL) |
| **Status** | PENDING, COMPLETED, CANCELLED, … |
| **Address (where)** | Customer ne form par jo **service address** likha — yahi “kahan” ka source hai |
| **Notes** | Form wala short description |

**Dhyaan:** App abhi booking row mein **GPS / IP / auto city** save **nahi** karti. “Kahan” = user ne jo **address** diya.

### Galat / missing setup

| Response | Matlab |
|----------|--------|
| **404** | `E2W_ADMIN_KEY` set nahi — admin feature band |
| **403** | Key galat ya missing |

### Data kahan se aa raha hai

- **MySQL configure** (`E2W_JDBC_URL`, `E2W_JDBC_USER`, …) → table `service_booking` se, newest pehle, max **500** rows.
- **MySQL nahi** → **in-memory** demo store; **server restart par data mit jata hai**.

---

## 2. Seedha database se (MySQL)

Jab production DB laga ho, admin **phpMyAdmin**, **MySQL Workbench**, ya koi BI tool se bhi dekh sakta hai.

Table: **`service_booking`** (schema: `web-ui/src/main/resources/db/schema.sql`)

Useful columns:

- `id`, `created_at`, `customer_name`, `phone`, `email`
- `service_type`, `status`
- `address`, `description`, `preferred_at`

Example query (last 100 bookings):

```sql
SELECT id, created_at, customer_name, phone, email,
       service_type, status, address, description
FROM service_booking
ORDER BY created_at DESC
LIMIT 100;
```

---

## 3. Security tips

- Demo key `easy2work-local-dev` **public** hai — sirf **local / no-DB** ke liye. Internet par deploy karte waqt **hamesha MySQL + `E2W_ADMIN_KEY`** use karein.
- `E2W_ADMIN_KEY` ko **kabhi git mein commit mat karein**; env / secret manager use karein.
- Sirf **trusted admins** ko key dein; zarurat ho to key rotate karein.
- Public internet par ho to **HTTPS** zaroor; aur ho sake to **IP allowlist** / VPN reverse proxy par rule lagayein.

---

## 4. Urdu / Hindi — chhota saransh

- **Bina MySQL (local):** admin URL `.../admin/bookings?key=easy2work-local-dev` — do browser, ek booking, ek admin refresh.
- **MySQL production:** `E2W_ADMIN_KEY` (ya `web.xml` `easy2workAdminKey`) zaroor; phir **`/admin/bookings?key=...`**.
- **“Kahan se”** = customer ne jo **pata (address)** likha; IP/GPS save nahi.
- **MySQL** = data permanent; **memory demo** = restart par gayab.

---

## 5. 404 / “Not Found” — troubleshooting

1. **URL mein `YOUR_SECRET` mat likho** — woh sirf documentation placeholder hai. Local (bina MySQL): key = `easy2work-local-dev`.
2. **Do URLs chal sakti hain:** `/admin/bookings` ya **`/admin-bookings`** (same servlet).
3. **MySQL env set hai** (`E2W_JDBC_URL`) lekin `E2W_ADMIN_KEY` nahi — pehle generic Jetty **404** dikhta tha; ab **503** + plain English instructions aani chahiye. Fix: `E2W_ADMIN_KEY` set karein, ya local demo ke liye DB env hata kar restart karein.
4. **Purana server:** `web-ui` folder se `mvn clean package` phir dubara `mvn jetty:run` — taaki naya `web.xml` aur servlet class load ho.

---

## Related code

- Servlet: `com.easy2work.web.servlet.AdminServlet`
- JSP: `web-ui/src/main/webapp/admin/bookings.jsp`
- URL mapping: `web-ui/src/main/webapp/WEB-INF/web.xml` → `/admin/bookings` and `/admin-bookings`
