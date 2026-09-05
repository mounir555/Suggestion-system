# SuggestionBox – Système de Boîte à Idées

A full-stack web application for managing employee improvement suggestions, built during a one-month internship in the IT department of **Lear Corporation – Tanger**.

The app lets employees submit ideas, track their status, and earn points when accepted, while admins review, accept/reject submissions, and manage users.

## Features

- **Authentication & role-based access** — single login page, redirecting to an employee or admin view (Spring Security)
- **Submit suggestions** — title, description, and optional file attachment as proof/reference
- **Track status** — Pending / Accepted / Rejected, with who processed it and when
- **Points system** — employees automatically earn 10 points when a suggestion is accepted
- **Admin dashboard** — view all suggestions company-wide, accept or reject pending ones
- **File uploads** — attachments handled via `multipart/form-data`
- **Full traceability** — timestamps and identities logged for every submission and decision

## Architecture

Three-layer architecture:

```
React.js (client)  →  Spring Boot REST API  →  MySQL database
      Axios/JSON            JPA/Hibernate
```

- **Frontend**: React.js + Bootstrap for a responsive UI
- **Backend**: Spring Boot exposing a REST API, layered as Controller → Service → Repository
- **Security**: Spring Security with `@PreAuthorize` for role-based endpoint protection (`USER` / `ADMIN`)
- **Database**: MySQL, accessed via JPA/Hibernate

### Data model

- **Utilisateur** — id, username, password, role (USER/ADMIN), points
- **Suggestion** — id, titre, description, statut (PENDING/ACCEPTED/REJECTED), dateDepot, dateTraitement, links to submitter and processor
- **PieceJointe** — attached file metadata, linked to a suggestion

## Tech stack

- Java, Spring Boot, Spring Security, JPA/Hibernate
- React.js, Bootstrap, Axios
- MySQL
- Postman (API testing during development)

## Getting started

### Backend
```bash
cd backend
./mvnw spring-boot:run
```
Configure your MySQL connection in `application.properties` before running.

### Frontend
```bash
cd frontend
npm install
npm run dev
```

By default the frontend runs on port 5173 (Vite) and the backend on port 8080 — CORS is configured in `SecurityConfig` to allow this.

## Notes

This project was built progressively over a 4-week internship: Java fundamentals → Spring Boot CRUD API → React frontend with auth → full "Idea Box" application with rewards, file handling, and traceability.
