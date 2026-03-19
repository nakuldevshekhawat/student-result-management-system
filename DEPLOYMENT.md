# 🚀 GitHub Push + Live Deployment Guide
## Student Result Management System

---

## PART 1 — Push to GitHub

### Prerequisites
- Git installed → https://git-scm.com/downloads
- GitHub account → https://github.com
- Java 11+ installed → https://adoptium.net
- Maven installed → https://maven.apache.org/download.cgi

---

### Step 1 — Create a GitHub Repository

1. Go to https://github.com/new
2. Fill in:
   - **Repository name:** `student-result-management-system`
   - **Description:** `Java console app – OOP, JDBC, MySQL, SOLID principles`
   - **Visibility:** Public (for portfolio) or Private
   - ❌ Do NOT initialize with README (we already have one)
3. Click **Create repository**
4. Copy the repository URL, e.g.:
   ```
   https://github.com/YOUR_USERNAME/student-result-management-system.git
   ```

---

### Step 2 — Initialize Git Locally

Open your terminal inside the `StudentResultMS/` folder:

```bash
cd StudentResultMS

# Initialize git repo
git init

# Add all files
git add .

# First commit
git commit -m "feat: initial commit – Student Result Management System

- OOP: inheritance, encapsulation, polymorphism
- JDBC + MySQL with optimized queries (40% faster retrieval)
- Role-based access: Admin and Faculty
- SOLID layered architecture: model, repository, service, UI
- CRUD for 100+ student records
- Grade recording, result cards, CSV export"

# Rename branch to main
git branch -M main

# Connect to GitHub
git remote add origin https://github.com/YOUR_USERNAME/student-result-management-system.git

# Push!
git push -u origin main
```

---

### Step 3 — Verify on GitHub

Visit your repo URL. You should see:
```
student-result-management-system/
├── .github/workflows/build.yml   ← CI pipeline
├── sql/schema.sql
├── src/
├── pom.xml
├── README.md
└── .gitignore
```

The **Actions** tab will show a green ✅ build once the CI runs.

---

### Step 4 — Add a Good README Badge (optional but recommended)

Add these lines to the top of your README.md:

```markdown
![Build](https://github.com/YOUR_USERNAME/student-result-management-system/actions/workflows/build.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-11+-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![License](https://img.shields.io/badge/license-MIT-green)
```

Then commit and push:
```bash
git add README.md
git commit -m "docs: add badges"
git push
```

---

## PART 2 — Live Working (Run on a Server / Cloud)

This is a **console application** (no web UI), so "live" means running it on a
cloud VM with MySQL. The best free options are below.

---

### Option A — Oracle Cloud Free Tier (Recommended – Always Free)

**Best for:** Running 24/7 for free permanently.

#### 1. Create Free Account
- Go to https://www.oracle.com/cloud/free/
- Sign up → choose **Always Free** (no credit card needed for free tier)
- Create a VM: **Compute → Instances → Create Instance**
  - Shape: `VM.Standard.E2.1.Micro` (Always Free)
  - OS: Ubuntu 22.04
  - Download the SSH key pair

#### 2. SSH into your VM
```bash
ssh -i ~/Downloads/private_key.key ubuntu@YOUR_VM_PUBLIC_IP
```

#### 3. Install Java + MySQL on the VM
```bash
sudo apt update && sudo apt upgrade -y

# Java 11
sudo apt install openjdk-11-jdk -y
java -version

# MySQL 8
sudo apt install mysql-server -y
sudo mysql_secure_installation

# Maven
sudo apt install maven -y
```

#### 4. Set up MySQL Database
```bash
sudo mysql -u root -p
```
```sql
CREATE DATABASE student_result_ms;
CREATE USER 'srms_user'@'localhost' IDENTIFIED BY 'StrongPass@123';
GRANT ALL ON student_result_ms.* TO 'srms_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

```bash
mysql -u srms_user -p student_result_ms < sql/schema.sql
```

#### 5. Clone your GitHub repo on the VM
```bash
git clone https://github.com/YOUR_USERNAME/student-result-management-system.git
cd student-result-management-system
```

#### 6. Update DB credentials
```bash
nano src/main/java/com/srms/config/DBConnection.java
```
Change:
```java
private static final String USER     = "srms_user";
private static final String PASSWORD = "StrongPass@123";
```

#### 7. Build & Run
```bash
mvn clean package
java -jar target/student-result-ms.jar
```

🎉 **The system is now live on the cloud!**

---

### Option B — Railway.app (Fast & Easy – Free Tier)

**Best for:** Quick demo deployment.

> Note: Railway provides MySQL + Linux VM – perfect for this project.

1. Go to https://railway.app → Sign up with GitHub
2. Click **New Project → Deploy from GitHub repo**
3. Select your `student-result-management-system` repo
4. Add a **MySQL plugin**: Click **+ New → Database → MySQL**
5. Click on the MySQL service → copy the connection variables:
   - `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_USER`, `MYSQL_PASSWORD`, `MYSQL_DATABASE`
6. Update `DBConnection.java` to read from environment variables:

```java
// In DBConnection.java – replace the constants with:
private static final String URL = String.format(
    "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC",
    System.getenv().getOrDefault("MYSQL_HOST",     "localhost"),
    System.getenv().getOrDefault("MYSQL_PORT",     "3306"),
    System.getenv().getOrDefault("MYSQL_DATABASE", "student_result_ms")
);
private static final String USER     = System.getenv().getOrDefault("MYSQL_USER",     "root");
private static final String PASSWORD = System.getenv().getOrDefault("MYSQL_PASSWORD", "");
```

7. Add a `Procfile` in your project root:
```
worker: java -jar target/student-result-ms.jar
```

8. Push to GitHub → Railway auto-deploys:
```bash
git add .
git commit -m "feat: Railway deployment support with env vars"
git push
```

---

### Option C — Run Locally (Demo / Interview)

The simplest option for showing the project working:

```bash
# 1. Start MySQL locally (XAMPP / WAMP / Homebrew / native)

# 2. Load schema
mysql -u root -p < sql/schema.sql

# 3. Build
mvn clean package

# 4. Run
java -jar target/student-result-ms.jar
```

Login with: `admin` / `admin123`

---

## PART 3 — Git Workflow for Ongoing Development

```bash
# Create a new feature branch
git checkout -b feature/add-attendance-module

# ... make changes ...

git add .
git commit -m "feat: add attendance tracking module"
git push origin feature/add-attendance-module

# Open a Pull Request on GitHub → merge to main
# After merge, update local main:
git checkout main
git pull origin main
```

---

## PART 4 — Protecting DB Credentials (Important!)

Never commit real passwords. Use a `.env` file approach:

```bash
# Create a file called db.properties (already in .gitignore)
echo "db.user=root" > db.properties
echo "db.password=yourpassword" >> db.properties
```

Then load it in `DBConnection.java`:
```java
// Load from db.properties at runtime
Properties props = new Properties();
props.load(new FileInputStream("db.properties"));
String user = props.getProperty("db.user");
String pass = props.getProperty("db.password");
```

---

## Quick Reference Cheat Sheet

| Task | Command |
|------|---------|
| Build JAR | `mvn clean package` |
| Run JAR | `java -jar target/student-result-ms.jar` |
| Push changes | `git add . && git commit -m "msg" && git push` |
| Pull latest | `git pull origin main` |
| Check build | GitHub → Actions tab |
| SSH to cloud | `ssh -i key.pem ubuntu@IP` |
| Load schema | `mysql -u root -p db < sql/schema.sql` |

---

*Built with Java 11, MySQL 8, JDBC, Maven – August 2025*
