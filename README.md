# Odyssey: Beyond the Horizon

Ocean-themed event portal and management platform engineered with Spring Boot, Thymeleaf, and Vanilla CSS. Built for the flagship annual entrepreneurship summit hosted by E-Cell at KKR & KSR Institute of Technology and Sciences (KITS).

---

## Key Features

- **Event Showcase:** 4-stage entrepreneurship journey, itinerary timeline, and countdown clock.
- **Dynamic Team Registration:** Supports teams of 3 to 5 participants, live team name uniqueness validation, and dynamic fee calculation (₹100/member: ₹300, ₹400, or ₹500).
- **Direct Database Image Storage:** Payment proof screenshots are stored directly inside the MySQL database as `LONGBLOB` data and served via `/register/payment-image/{id}`. **No external cloud buckets or persistent volumes required!**
- **Holographic Digital Boarding Pass:** Dynamic ticketing with unique alphanumeric IDs (`ODY-2026-XXXX`) and one-click iCalendar (`.ics`) sync.
- **Self-Service Ticket Retrieval:** 4-factor credential verification (`/find-ticket`) for participants who lost their boarding pass link.
- **Verified Student Feedback:** Review submission portal (`/student-response`) restricted to verified registered teams.
- **Admin Command Center:** Secure dashboard (`/admin/registrations`) with 30-minute inactivity auto-logout, real-time participant/revenue/department metrics, payment proof viewer, record deletion, and Excel-ready CSV export.

---

## Production Deployment (Railway + MySQL)

The application is deployed on **Railway** connected to a **Railway MySQL** database.

### 1. Database Configuration
The application automatically binds to Railway's MySQL environment variables:
- `MYSQL_URL`: e.g. `jdbc:mysql://<host>:<port>/railway`
- `MYSQLUSER`: MySQL user (default `root`)
- `MYSQLPASSWORD`: Database password

Hibernate auto-creates and updates the tables on startup (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Payment Screenshot Storage in MySQL
Payment screenshots uploaded during registration are stored **directly in the MySQL database** in the `registrations` table:
- Column `payment_image_bytes`: `LONGBLOB` (supports images up to 10MB)
- Column `payment_image_type`: `VARCHAR(64)` (e.g., `image/png`, `image/jpeg`)
- Column `payment_image_path`: Set to `/register/payment-image/{groupId}`

> [!TIP]
> Because images are stored in MySQL, **container restarts and redeployments on Railway will NEVER delete or lose uploaded payment proofs**.

### 3. Environment Variables on Railway

Set these in your Railway web service **Variables** tab:

| Key | Value / Description | Default / Source |
|---|---|---|
| `MYSQL_URL` | `jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}` | Auto-provided by Railway MySQL |
| `MYSQLUSER` | `${{MySQL.MYSQLUSER}}` | Auto-provided by Railway MySQL |
| `MYSQLPASSWORD` | `${{MySQL.MYSQLPASSWORD}}` | Auto-provided by Railway MySQL |
| `ADMIN_USERNAME` | Admin login username | 
| `ADMIN_PASSWORD` | Admin login password | `Toinfinityandbeyond` |
| `PORT` | Web server port | Auto-bound by Railway (8080) |

---

## Local Development

### Prerequisites
- **Java 17+** (JDK 17 LTS recommended)
- **Maven 3.8+**
- **MySQL 8.0+** (or connect directly to your Railway MySQL instance)

### Run Locally
```bash
mvn spring-boot:run
```
Then open `http://localhost:8080` in your web browser.

---

## Web Endpoints

### Public Web Portal
- **Home Portal:** `http://localhost:8080/`
- **Registration Form:** `http://localhost:8080/register`
- **Boarding Pass Ticket:** `http://localhost:8080/register/pass/{id}`
- **Find Lost Ticket:** `http://localhost:8080/find-ticket`
- **Event Schedule & Timeline:** `http://localhost:8080/calendar`
- **Knowledge Base & FAQ:** `http://localhost:8080/faq`
- **Student Voice & Feedback:** `http://localhost:8080/student-response`

### Administrative Portal (Protected by Session Auth)
- **Admin Login:** `http://localhost:8080/admin/login`
- **Admin Registrations Manifest:** `http://localhost:8080/admin/registrations`
- **Admin Feedback Moderation:** `http://localhost:8080/admin/responses`
- **Registrations CSV Export:** `http://localhost:8080/admin/export/csv`
- **Feedback CSV Export:** `http://localhost:8080/admin/responses/export/csv`
- **Payment Image Stream:** `http://localhost:8080/register/payment-image/{id}`

### REST APIs
- `GET /api/check-team-name?name={name}`: Live team name availability check
- `GET /api/registrations`: JSON list of all registrations (Admin session required)
- `GET /api/dept-counts`: JSON department participant aggregations (Admin session required)
- `POST /api/admin/registrations/delete?id={id}`: API endpoint for record deletion

---

## Architecture & Code Structure

```
src/main/java/com/ecell/odyssey/
├── OdysseyApplication.java          # Spring Boot Application entrypoint
├── HomeController.java              # Routes: /, /calendar
├── Registration.java                # JPA Entity for `registrations` (includes LONGBLOB image bytes)
├── RegistrationRepository.java      # Spring Data JPA queries
├── RegistrationService.java         # Business logic: team decomposition, validation, deletion
├── RegistrationController.java      # Routes: /register, /pass, /find-ticket, /admin, image streaming
├── StudentResponse.java             # JPA Entity for `student_responses`
├── StudentResponseRepository.java  # Feedback repository queries
├── StudentResponseController.java  # Routes: /student-response, /admin/responses, CSV export
├── WebConfig.java                   # Resource handlers & view controllers
└── SupabaseStorageService.java      # Optional cloud storage service (fallback to MySQL)

src/main/resources/
├── application.properties           # App & database configuration (MySQL, HikariCP, Sessions)
├── static/
│   ├── css/                         # Ocean Cyberpunk design tokens & stylesheets
│   └── js/                          # Interactive client validation & UI scripts
└── templates/                       # Thymeleaf server-rendered HTML templates
```

---

## Specifications

Detailed IEEE 830 compliant software requirements are documented in **[SRS.md](file:///d:/subbu/kits/E-Cell/ecell-site/kits-ecell/odyssey/SRS.md)**.
