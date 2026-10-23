# Odyssey: Beyond the Horizon

Ocean-themed event website built with Spring Boot, Thymeleaf, HTML and CSS.

## Run (local development)

Requires Java 17+ and Maven.

    mvn spring-boot:run

Then open http://localhost:8080

Local defaults expect a PostgreSQL database at `jdbc:postgresql://localhost:5432/odyssey`
(user `postgres`, password `postgres`). Override with the `DB_URL`, `DB_USERNAME`,
`DB_PASSWORD` environment variables. Without them (and without Supabase Storage
configured) the app falls back to local `uploads/` for payment screenshots.

## Free production deployment (Render + Supabase)

Everything runs on free tiers: **Render** (hosting), **Supabase** (PostgreSQL
database + Storage for payment screenshots), **GitHub** (source code).

### 1. Supabase — free database + storage

1. Create a project at https://supabase.com (free tier).
2. In **Project Settings → Database**, copy the connection info:
   - Host: `db.<project-ref>.supabase.co`, port `5432`, database `postgres`, user `postgres`
   - **DB_URL** = `jdbc:postgresql://db.<project-ref>.supabase.co:5432/postgres?sslmode=require`
3. In **Storage**, create a bucket named `payments` and turn **Public bucket** ON.
4. In **Project Settings → API**, copy the **Project URL** and the **`service_role` secret**.

The app creates its tables automatically on first start (`spring.jpa.hibernate.ddl-auto=update`).

### 2. GitHub

Push this repository to GitHub (it already has `origin` configured):

    git push origin main

### 3. Render — free web service

1. https://dashboard.render.com → **New → Web Service** → connect the GitHub repo.
2. Render detects `render.yaml` (build `mvn -DskipTests clean package`, start
   `java -jar target/odyssey-1.0.0.jar`, free plan, Java 17).
3. Under **Environment**, set:

   | Key | Value |
   |---|---|
   | `DB_URL` | `jdbc:postgresql://db.<ref>.supabase.co:5432/postgres?sslmode=require` |
   | `DB_USERNAME` | `postgres` |
   | `DB_PASSWORD` | your Supabase database password |
   | `SUPABASE_URL` | `https://<project-ref>.supabase.co` |
   | `SUPABASE_SERVICE_ROLE_KEY` | the `service_role` secret |
   | `SUPABASE_BUCKET` | `payments` |
   | `ADMIN_USERNAME` | admin login username |
   | `ADMIN_PASSWORD` | admin login password (choose a strong one) |

4. Deploy. Your site will be live at `https://odyssey-xxxx.onrender.com`.

Notes:
- Render free services **sleep** after ~15 min idle; the first request after idle
  takes ~30 s. Subsequent requests are fast.
- The Render disk is **ephemeral** — that is why payment screenshots go to
  Supabase Storage (permanent URLs). Local `uploads/` is only a fallback.
- Remember to **delete the old AWS RDS instance** (it bills hourly) and rotate the
  database password that was previously committed to git.

## Structure

- `HomeController.java` holds the event name, tagline and the four stages
- `Registration.java` is the JPA entity mapped to the `registrations` database table
- `RegistrationRepository.java` is the Spring Data JPA repository
- `RegistrationService.java` manages database persistence and queries
- `RegistrationController.java` handles `/register`, `/register/pass/{id}`, `/admin/registrations`, and `/api/registrations`
- `SupabaseStorageService.java` uploads payment screenshots to Supabase Storage
- `templates/index.html` is the home page
- `templates/register.html` is the Odyssey registration page
- `templates/pass.html` is the digital holographic boarding pass ticket
- `templates/admin.html` is the database manifest dashboard
- `static/css/style.css` is the base ocean theme
- `static/css/register.css` contains registration form and boarding pass styles
- `static/js/main.js` is the scroll reveal animation
- `static/js/register.js` manages track selection, team size dynamic fields, and form interaction
- `render.yaml` is the Render deployment blueprint

## Database & Endpoints

- **Web Portal**: http://localhost:8080/
- **Registration Form**: http://localhost:8080/register
- **Database Manifest Dashboard**: http://localhost:8080/admin/registrations
- **JSON API**: http://localhost:8080/api/registrations
- **Database Engine**: PostgreSQL (Supabase free tier)
- **File Storage**: Supabase Storage (bucket `payments`)

All secrets are provided via environment variables — never commit credentials.
# Odyssey
