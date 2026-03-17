# Deployment Guide — Workout Tracker

This document walks through every step required to go from a fresh download of this repository to a fully running application. No prior knowledge of the project is assumed.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Prerequisites](#2-prerequisites)
3. [Download & Unpack the Project](#3-download--unpack-the-project)
4. [Option A — Docker (Recommended)](#4-option-a--docker-recommended)
5. [Option B — Manual Local Setup](#5-option-b--manual-local-setup)
   - [5.1 Database Setup (MySQL)](#51-database-setup-mysql)
   - [5.2 Backend Setup (Spring Boot)](#52-backend-setup-spring-boot)
   - [5.3 Frontend Setup (Angular)](#53-frontend-setup-angular)
6. [Verifying the Setup](#6-verifying-the-setup)
7. [Troubleshooting](#7-troubleshooting)

---

## 1. Project Overview

The Workout Tracker is a three-tier web application:

| Database | MySQL 8.0 | 3306 |
| Backend (API) | Java 17 + Spring Boot 3.2.2 (Maven) | 8080 |
| Frontend (UI) | Angular 19 + Node.js 20 | 4200 (dev) / 4000 (SSR) |

The backend exposes a REST API. The frontend consumes it through Angular services. Authentication is session-based; most routes require a login. An admin account has access to all users' data; regular users see only their own.

---

## 2. Prerequisites

Install all of the following before proceeding. Version minimums are listed; newer versions are generally fine.

### 2.1 For Docker deployment (Option A)

| Docker Desktop | version: 24.x | https://www.docker.com/products/docker-desktop |

Docker Desktop bundles Docker Engine and Docker Compose. No other tools are required for Option A.

**macOS / Windows**: Download the installer from the link above and run it. Accept default settings.
**Linux**: Follow the official Docker Engine install guide for your distro, then install the `docker-compose-plugin`.

Verify the installation:
```bash
docker --version        # Docker version 24.x or higher
docker compose version  # Docker Compose version v2.x or higher
```

### 2.2 For manual local deployment (Option B)

| Tool | Minimum Version | Download |
|------|----------------|---------|
| Java JDK | 17 | https://adoptium.net (Eclipse Temurin 17 recommended) |
| Apache Maven | 3.9 | https://maven.apache.org/download.cgi |
| Node.js | 20 LTS | https://nodejs.org |
| Angular CLI | 19 | Install via npm after Node.js |
| MySQL Server | 8.0 | https://dev.mysql.com/downloads/mysql/ |
| MySQL Workbench (optional) | 8.x | https://dev.mysql.com/downloads/workbench/ |
| make | any | Pre-installed on macOS/Linux; Windows: install via WSL or Git Bash |

**Java (JDK 17)**: Download the Eclipse Temurin 17 installer from https://adoptium.net. Run it and follow the prompts. Make sure `JAVA_HOME` is set and `java` is on your `PATH`:
```bash
java -version   # should print openjdk 17.x.x
```

**Maven**: Download the binary zip from https://maven.apache.org/download.cgi. Extract it to a permanent location (e.g., `/opt/maven` or `C:\tools\maven`). Add the `bin/` directory to your `PATH`. Then verify:
```bash
mvn -version    # should print Apache Maven 3.9.x
```

**Node.js 20 LTS**: Download and run the installer from https://nodejs.org. This also installs `npm`. Verify:
```bash
node --version  # v20.x.x
npm --version   # 10.x.x
```

**Angular CLI**: After Node.js is installed, run:
```bash
npm install -g @angular/cli
ng version      # Angular CLI: 19.x.x
```

**MySQL 8.0**: Download the Community Server installer from https://dev.mysql.com/downloads/mysql/. During installation:
- Choose "Developer Default" or "Server Only"
- Set the root password to something you will remember (you will use it in step 5.1)
- Leave the default port as **3306**
- Enable MySQL as a system service so it starts automatically

Optionally install **MySQL Workbench** for a graphical interface to the database.

---

## 3. Download & Unpack the Project

### From GitHub (ZIP download) (option A)

1. Go to the repository on GitHub.
2. Click the green **Code** button → **Download ZIP**.
3. Save the file (e.g., `548-main.zip`) to a convenient location such as your Desktop or `~/Projects`.
4. Extract the ZIP:
   - **macOS / Linux**: Double-click the ZIP in Finder/Files, or run: `unzip 548-main.zip`
   - **Windows**: Right-click the ZIP → Extract All

After extraction you will have a folder structure like this:
```
548-main/
├── backend/        ← Spring Boot API
├── frontend/       ← Angular UI
├── queries/        ← SQL schema and seed data
├── Makefile        ← Convenience build/run commands
├── docker-compose.yml
└── README.md
```

All subsequent commands should be run from the **root** of this extracted folder (`548-main/`) unless otherwise stated.

### From Git (clone) (option B)

```bash
git clone <repository-url>
cd 548
```

---

## 4. Option A — Docker (Recommended)

This is the fastest path. Docker builds and wires up all three tiers automatically.

### Step 1 — Start Docker Desktop

Open Docker Desktop and wait until the whale icon in your system tray/menu bar shows "Docker Desktop is running."

### Step 2 — Initialize the database

The Docker Compose file starts a fresh MySQL instance, but the application's schema and seed data still need to be loaded. Do this once before first use:

1. In a terminal, from the project root, bring up just the database container:
   ```bash
   docker compose up db -d
   ```
2. Wait about 10–15 seconds for MySQL to initialize, then load the schema:
   ```bash
   docker exec -i 548-db-1 mysql -u root -prootpassword < queries/buildingDB.sql
   ```
   > If the container name differs, run `docker ps` and use the name shown in the NAME column for the `db` service.

3. Load the password migration:
   ```bash
   docker exec -i 548-db-1 mysql -u root -prootpassword < queries/0.11.sql
   ```

### Step 3 — Configure the backend secret properties

The backend requires a file at `backend/src/main/resources/application-secret.properties`. This file is excluded from the repository (it is in `.gitignore`). Create it now:

```bash
# From the project root:
cat > backend/src/main/resources/application-secret.properties << 'EOF'
db.url=jdbc:mysql://db:3306/WorkoutTracker
db.username=root
db.password=rootpassword
app.admin.name=username
EOF
```

> On Windows (PowerShell), create the file manually in a text editor with the four lines above.

### Step 4 — Build and start all services

```bash
docker compose up --build
```

This command:
- Builds the backend Docker image (compiles the Java/Maven project inside the container)
- Builds the frontend Docker image (installs npm packages and builds the Angular app inside the container)
- Starts the database, then the backend (once the database is healthy), then the frontend

The first run takes several minutes because it downloads base images and compiles both projects. Subsequent runs are much faster.

### Step 5 — Open the application

Once you see log output indicating the Spring Boot server has started (look for `Started App in X.XX seconds`) and the frontend container is running, open:

```
http://localhost:4000
```

You should see the Workout Tracker login page.

### Stopping

```bash
docker compose down
```

To also remove all stored data (wipe the database volume):
```bash
docker compose down -v
```

---

## 5. Option B — Manual Local Setup

Use this option if you prefer to run services directly on your machine without Docker, or if you want to develop and modify the code.

### 5.1 Database Setup (MySQL)

#### Start MySQL

Make sure the MySQL service is running. On most systems:
```bash
# macOS (Homebrew)
brew services start mysql

# Linux (systemd)
sudo systemctl start mysql

# Windows
# Start via the Services panel or:
net start MySQL80
```

#### Create the database and load the schema

Open a terminal (or MySQL Workbench Query tab) and connect to MySQL as root:
```bash
mysql -u root -p
```
Enter your root password when prompted.

Then run:
```sql
SOURCE /full/path/to/548-main/queries/buildingDB.sql;
SOURCE /full/path/to/548-main/queries/0.11.sql;
EXIT;
```

Replace `/full/path/to/548-main/` with the actual path to the project root.

Alternatively, from your terminal without entering the MySQL shell:
```bash
mysql -u root -p < queries/buildingDB.sql
mysql -u root -p < queries/0.11.sql
```

After running these scripts the database `WorkoutTracker` will exist with all tables created and populated with sample data (6 users, 8 muscle groups, 10 goals, 5 injuries, 10 workouts, 22 exercises).

#### Verify the database

```bash
mysql -u root -p -e "USE WorkoutTracker; SHOW TABLES; SELECT COUNT(*) FROM APPUSER;"
```

Expected output includes 6 tables and a count of 6 users.

### 5.2 Backend Setup (Spring Boot)

#### Create the secret properties file

The backend reads database credentials from `application-secret.properties`, which is not committed to the repository. Create it at:
```
backend/src/main/resources/application-secret.properties
```

with the following contents (adjust the password to match your MySQL root password):
```properties
db.url=jdbc:mysql://localhost:3306/WorkoutTracker
db.username=root
db.password=YOUR_MYSQL_ROOT_PASSWORD
app.admin.name=William Thacher
```

> **Important**: `app.admin.name` must match exactly one of the `NAME` values in the `APPUSER` table. The default admin is `William Thacher`. This user gets access to all data in the application.

#### Compile the backend

From the project root:
```bash
make install
```

This runs `mvn clean compile` inside the `backend/` directory. You should see `BUILD SUCCESS` at the end.

If `make` is not available (Windows without WSL), run the equivalent directly:
```bash
cd backend
mvn clean compile
cd ..
```

#### Run the backend

```bash
make run-back
```

Equivalent without make:
```bash
cd backend
mvn exec:java -Dexec.mainClass="com.myapp.App"
```

The backend is ready when you see output containing:
```
Started App in X.XXX seconds (process running for X.XXX)
```

The API is now listening at `http://localhost:8080`.

Leave this terminal window open and running.

### 5.3 Frontend Setup (Angular)

Open a **new terminal window** (keep the backend running in the first one).

#### Install dependencies

```bash
cd frontend
npm install
```

This downloads and installs all Angular and Node.js dependencies into the `node_modules/` folder. It takes a minute or two on first run.

#### Run the development server

```bash
ng serve
```

Or from the project root using make:
```bash
make run-front
```

The Angular CLI will compile the application and print:
```
** Angular Live Development Server is listening on localhost:4200 **
```

#### Run both at once (shortcut)

From the project root, a single command starts both the backend and frontend in parallel:
```bash
make run-all
```

Both processes run in the same terminal. Use `Ctrl+C` to stop both.

---

## 6. Verifying the Setup

### Check that services are up

| Service | URL | Expected Response |
|---------|-----|------------------|
| Frontend (dev) | http://localhost:4200 | Login page loads in browser |
| Frontend (Docker/SSR) | http://localhost:4000 | Login page loads in browser |
| Backend API health | http://localhost:8080/api/users | JSON array of users (if logged in) |

### Log in to the application

1. Navigate to `http://localhost:4200` (manual) or `http://localhost:4000` (Docker).
2. You will see a login form.
3. Check DB for usernames and passwords

4. After logging in you will be redirected to the workouts page. The navigation bar at the top provides access to Workouts, Exercises, Goals, Injuries, and Users (admin only).

### Verify data is visible

- **Workouts page** should show workouts for the logged-in user (or all users if admin).
- **Exercises page** shows exercise logs.
- **Goals page** shows goals.
- **Injuries page** shows injury records.
- **Users page** (admin only) shows all 6 seeded user accounts.

If all pages load data correctly, the setup is complete and working.

---

## 7. Troubleshooting

### "Cannot connect to database" / backend fails to start

- Confirm MySQL is running: `mysql -u root -p -e "SELECT 1;"`
- Confirm the database exists: `mysql -u root -p -e "SHOW DATABASES;"`
- Double-check the values in `application-secret.properties` — especially `db.password` and `db.url`.
- If using Docker, make sure the `db.url` uses `db` as the hostname (not `localhost`): `jdbc:mysql://db:3306/WorkoutTracker`.

### "Port 3306 already in use"

Another MySQL instance is already running, or another process is using the port. Stop the other process or change the port in both MySQL config and `application-secret.properties`.

### "Port 8080 already in use"

Something else is using port 8080. Kill it:
```bash
# macOS / Linux
lsof -i :8080
kill -9 <PID>
```
Or change `server.port` in `application.properties` and update the frontend's API base URL in the Angular environment files.

### Angular `ng` command not found

The Angular CLI was not installed globally. Run:
```bash
npm install -g @angular/cli
```

### Maven `mvn` command not found

Maven is not on your `PATH`. Ensure the Maven `bin/` directory is in your `PATH` environment variable. See the Prerequisites section for setup instructions.

### Docker build fails ("no space left on device" or similar)

Run `docker system prune` to remove unused images and containers, then try again.

### Login fails / "Invalid credentials"

- Confirm the SQL scripts ran successfully (both `buildingDB.sql` and `0.11.sql`).
- The `0.11.sql` migration adds the `password` column; without it, all logins will fail.
- Verify the `APPUSER` table has a `password` column: `DESCRIBE WorkoutTracker.APPUSER;`

### CORS errors in the browser console

The backend allows CORS from `http://localhost:4200` and `http://localhost:4000` by default (configured in `WebConfig.java`). If you are running the frontend on a different port, add that origin to `WebConfig.java` and restart the backend.
