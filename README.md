# Task_1 — Full Stack App (Spring Boot + Angular)

A simple full-stack project built with **Spring Boot (Java)** for the backend and **Angular** for the frontend.  
It supports user registration, login, article creation, likes/dislikes, and comments.

---

## Project Structure
Task_1/
├── backend/ # Spring Boot backend API
└── frontend/ # Angular frontend app

## How to Run

### 1- Backend (Spring Boot)
**Requirements:** Java 17+ and Maven installed.

```bash
cd backend
mvn spring-boot:run
The backend will start at:
http://localhost:8080
```

### 1- Frontend (Angular)
**Requirements:**  Node.js and npm installed.

```bash
cd frontend
npm install
npm start
The frontend will start at:
http://localhost:5173
```


Default Admin User
Username	Password
admin	admin12345
Created automatically when the backend first runs.


API Overview

| Method   | Endpoint                | Description                   |
| -------- | ----------------------- | ----------------------------- |
| `POST`   | `/user`                 | Register a new user           |
| `GET`    | `/login`                | Login (Basic Auth)            |
| `GET`    | `/article`              | List all articles             |
| `GET`    | `/article/{id}`         | Get article by ID             |
| `POST`   | `/article`              | Create a new article *(USER)* |
| `DELETE` | `/article/{id}`         | Delete own article *(USER)*   |
| `PUT`    | `/article/{id}/like`    | Like article *(USER)*         |
| `PUT`    | `/article/{id}/dislike` | Dislike article *(USER)*      |
| `PUT`    | `/article/{id}/disable` | Disable article *(ADMIN)*     |
| `PUT`    | `/article/{id}/enable`  | Enable article *(ADMIN)*      |


