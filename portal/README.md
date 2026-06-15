# Placement & Internship Management Portal

A backend application to manage student placements and internships, built with Spring Boot and MySQL.

---

## Tech Stack

| Technology      | Version |
|-----------------|---------|
| Java            | 17      |
| Spring Boot     | 3.3.x   |
| Spring Data JPA | 3.3.x   |
| MySQL           | 8.x     |
| Lombok          | Latest  |
| Maven           | 3.x     |

---

## Design Patterns

- **Singleton Pattern** — `NotificationManager` ensures a single shared notification instance across the entire application lifecycle
- **Factory Pattern** — `PostingFactory` decides and creates the correct posting object (`JobPosting` or `InternshipPosting`) based on the posting type

---

## Features

- Company registration and management
- Job and internship posting with branch and CGPA eligibility filters
- Student registration and application to postings
- Eligibility check — students see only relevant opportunities based on their branch and CGPA
- Manual review mode — companies can allow below-cutoff students to apply
- Shortlisting — single application shortlisting and bulk shortlisting by CGPA threshold
- Multi-round interview scheduling with automatic round number assignment
- Round progression — next round is only allowed if the previous round result is PASSED
- Application status tracking across the full placement lifecycle
- Placement reports — overall summary, branch-wise, company-wise, student-wise, and interview success rates
- Notification logging via Singleton pattern

---

## Project Structure

```
src/main/java/com/placement/portal/
├── controller        → REST API endpoints
├── service           → Business logic layer
├── repository        → JPA repositories and custom queries
├── model             → JPA entities and enums
├── dto               → Request and response objects
├── exception         → Custom exceptions and global exception handler
└── config            → Factory and Singleton design pattern classes
```

---

## Setup Instructions

### Prerequisites

- Java 17 or higher
- MySQL 8.x
- Maven 3.x
- IntelliJ IDEA (recommended)

### Step 1 — Database Setup

Open MySQL and run:

```sql
CREATE DATABASE placement_portal;
```

### Step 2 — Configure application.properties

Open `src/main/resources/application.properties` and update your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/placement_portal?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

spring.application.name=placement-portal
server.port=8080
```

### Step 3 — Run the Application

```bash
mvn spring-boot:run
```

The server starts at `http://localhost:8080`

> Tables are auto-created by Hibernate on first run — no SQL scripts needed.

---

## API Endpoints

### Companies

| Method | URL                   | Description        |
|--------|-----------------------|--------------------|
| POST   | `/api/companies`      | Register a company |
| GET    | `/api/companies`      | Get all companies  |
| GET    | `/api/companies/{id}` | Get company by ID  |
| PUT    | `/api/companies/{id}` | Update company     |
| DELETE | `/api/companies/{id}` | Delete company     |

### Students

| Method | URL                  | Description        |
|--------|----------------------|--------------------|
| POST   | `/api/students`      | Register a student |
| GET    | `/api/students`      | Get all students   |
| GET    | `/api/students/{id}` | Get student by ID  |
| PUT    | `/api/students/{id}` | Update student     |
| DELETE | `/api/students/{id}` | Delete student     |

### Job Postings

| Method | URL                                      | Description                     |
|--------|------------------------------------------|---------------------------------|
| POST   | `/api/job-postings`                      | Create a job posting            |
| GET    | `/api/job-postings`                      | Get all job postings            |
| GET    | `/api/job-postings/{id}`                 | Get job posting by ID           |
| GET    | `/api/job-postings/eligible/{studentId}` | Get eligible jobs for a student |
| GET    | `/api/job-postings/company/{companyId}`  | Get jobs posted by a company    |
| PUT    | `/api/job-postings/{id}`                 | Update job posting              |
| DELETE | `/api/job-postings/{id}`                 | Delete job posting              |

### Internship Postings

| Method | URL                                             | Description                            |
|--------|-------------------------------------------------|----------------------------------------|
| POST   | `/api/internship-postings`                      | Create an internship posting           |
| GET    | `/api/internship-postings`                      | Get all internship postings            |
| GET    | `/api/internship-postings/{id}`                 | Get internship posting by ID           |
| GET    | `/api/internship-postings/eligible/{studentId}` | Get eligible internships for a student |
| GET    | `/api/internship-postings/company/{companyId}`  | Get internships posted by a company    |
| PUT    | `/api/internship-postings/{id}`                 | Update internship posting              |
| DELETE | `/api/internship-postings/{id}`                 | Delete internship posting              |

### Applications

| Method | URL                                                            | Description                        |
|--------|----------------------------------------------------------------|------------------------------------|
| POST   | `/api/applications`                                            | Student applies to a posting       |
| GET    | `/api/applications`                                            | Get all applications               |
| GET    | `/api/applications/{id}`                                       | Get application by ID              |
| GET    | `/api/applications/student/{studentId}`                        | Get all applications by a student  |
| GET    | `/api/applications/job/{jobPostingId}`                         | Get applications for a job posting |
| GET    | `/api/applications/internship/{internshipPostingId}`           | Get applications for an internship |
| PUT    | `/api/applications/{id}/shortlist`                             | Shortlist an application           |
| PUT    | `/api/applications/{id}/select`                                | Mark student as selected           |
| PUT    | `/api/applications/{id}/reject`                                | Reject an application              |
| PUT    | `/api/applications/job/{jobPostingId}/bulk-shortlist?minCgpa=` | Bulk shortlist by CGPA cutoff      |

### Interviews

| Method | URL                                           | Description                               |
|--------|-----------------------------------------------|-------------------------------------------|
| POST   | `/api/interviews`                             | Schedule an interview round               |
| GET    | `/api/interviews/student/{studentId}`         | Get all interviews for a student          |
| GET    | `/api/interviews/application/{applicationId}` | Get full round history for an application |
| PUT    | `/api/interviews/{id}/result`                 | Update interview round result             |

### Reports

| Method | URL                         | Description                          |
|--------|-----------------------------|--------------------------------------|
| GET    | `/api/reports/summary`      | Overall placement summary            |
| GET    | `/api/reports/branch-wise`  | Branch-wise placement statistics     |
| GET    | `/api/reports/company-wise` | Company-wise placement statistics    |
| GET    | `/api/reports/students`     | Student-wise placement status report |
| GET    | `/api/reports/interviews`   | Interview success rate report        |

### Notifications

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/notifications` | View all system notifications |

---

## Application Status Flow

```
APPLIED → SHORTLISTED → INTERVIEW_SCHEDULED → SELECTED
                                            → REJECTED
```

## Interview Round Flow

```
Schedule Round 1 → Update Result
                       ↓ PASSED → Schedule Round 2 → Update Result → ...→ SELECTED
                       ↓ FAILED → Application marked REJECTED
```

---

## Sample Request Bodies

### Register a Company
```json
{
  "name": "TechCorp Solutions",
  "domain": "Product",
  "email": "hr@techcorp.com",
  "location": "Hyderabad",
  "description": "Leading product-based software company"
}
```

### Register a Student
```json
{
  "name": "Hima Reddy",
  "email": "hima@college.edu",
  "branch": "CSE",
  "cgpa": 8.9,
  "skills": "Java, Spring Boot, MySQL",
  "resumeUrl": "https://drive.google.com/resume"
}
```

### Create a Job Posting
```json
{
  "title": "Backend Developer",
  "description": "Build scalable REST APIs using Spring Boot",
  "requiredSkills": "Java, Spring Boot, MySQL",
  "ctc": 12.0,
  "jobType": "Full-time",
  "location": "Hyderabad",
  "deadline": "2026-07-15",
  "companyId": 1,
  "eligibleBranches": "CSE,IT",
  "minCgpa": 7.0,
  "manualReviewEnabled": false
}
```

### Student Applies to a Job
```json
{
  "studentId": 1,
  "jobPostingId": 1
}
```

### Schedule an Interview
```json
{
  "applicationId": 1,
  "scheduledAt": "2026-06-20T10:30:00",
  "mode": "Online",
  "feedback": "Round 1 - Technical screening"
}
```

### Update Interview Result
```json
{
  "result": "PASSED",
  "feedback": "Excellent DSA skills, proceed to round 2"
}
```

---

## Entities

| Entity            | Table               | Description                      |
|-------------------|---------------------|----------------------------------|
| Company           | companies           | Registered companies             |
| Student           | students            | Registered students              |
| JobPosting        | job_postings        | Jobs posted by companies         |
| InternshipPosting | internship_postings | Internships posted by companies  |
| Application       | applications        | Student applications to postings |
| Interview         | interviews          | Interview rounds per application |

---

## Enums

| Enum              | Values                                                        |
|-------------------|---------------------------------------------------------------|
| PostingStatus     | OPEN, CLOSED, FILLED                                          |
| ApplicationStatus | APPLIED, SHORTLISTED, INTERVIEW_SCHEDULED, SELECTED, REJECTED |
| InterviewResult   | PENDING, PASSED, FAILED                                       |
| EligibilityStatus | ELIGIBLE, CGPA_BELOW_CUTOFF, NOT_ELIGIBLE                     |