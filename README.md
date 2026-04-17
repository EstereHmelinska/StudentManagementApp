# Student Management System

This is a web application built with **Spring Boot, Thymeleaf, and MySQL**.

It allows managing students, courses, and users.

---

## 🔧 Requirements

* Java 17+
* Maven
* MySQL

---

## ▶️ How to run the project

### 1. Clone repository

```bash
git clone https://github.com/EstereHmelinska/StudentManagementApp.git
cd StudentManagementApp
```

---

### 2. Create database in MySQL

```sql
CREATE DATABASE student_management;
```

---

### 3. Update database credentials

Open:

```text
src/main/resources/application.properties
```

Edit:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

---

### 4. Run the application

```bash
mvn spring-boot:run
```

---

### 5. Open in browser

```text
http://localhost:8080
```

---

## 📁 Project structure

* controller → handles requests
* service → business logic
* repository → database access
* entity → database models
* templates → HTML pages
* static → CSS / images

---

## 👩‍💻 Author

Estere Hmelinska
