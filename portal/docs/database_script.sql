-- ============================================================
-- Placement & Internship Management Portal
-- Database Script
-- ============================================================

-- Create and select database
CREATE DATABASE IF NOT EXISTS placement_portal;
USE placement_portal;

-- ============================================================
-- Table: companies
-- ============================================================
CREATE TABLE IF NOT EXISTS companies (
                                         id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         name        VARCHAR(255) NOT NULL,
    domain      VARCHAR(255),
    email       VARCHAR(255) UNIQUE,
    location    VARCHAR(255),
    description TEXT,
    created_at  DATETIME,
    updated_at  DATETIME
    );

-- ============================================================
-- Table: students
-- ============================================================
CREATE TABLE IF NOT EXISTS students (
                                        id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    branch      VARCHAR(100),
    cgpa        DOUBLE,
    skills      VARCHAR(500),
    resume_url  VARCHAR(500),
    created_at  DATETIME,
    updated_at  DATETIME
    );

-- ============================================================
-- Table: job_postings
-- ============================================================
CREATE TABLE IF NOT EXISTS job_postings (
                                            id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            company_id            BIGINT NOT NULL,
                                            title                 VARCHAR(255) NOT NULL,
    description           TEXT,
    required_skills       VARCHAR(500),
    eligible_branches     VARCHAR(255),
    min_cgpa              DOUBLE,
    manual_review_enabled BOOLEAN DEFAULT FALSE,
    status                VARCHAR(50) DEFAULT 'OPEN',
    ctc                   DOUBLE,
    job_type              VARCHAR(100),
    location              VARCHAR(255),
    deadline              DATE,
    created_at            DATETIME,
    updated_at            DATETIME,
    CONSTRAINT fk_job_company FOREIGN KEY (company_id) REFERENCES companies(id)
    );

-- ============================================================
-- Table: internship_postings
-- ============================================================
CREATE TABLE IF NOT EXISTS internship_postings (
                                                   id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                   company_id            BIGINT NOT NULL,
                                                   title                 VARCHAR(255) NOT NULL,
    description           TEXT,
    required_skills       VARCHAR(500),
    eligible_branches     VARCHAR(255),
    min_cgpa              DOUBLE,
    manual_review_enabled BOOLEAN DEFAULT FALSE,
    status                VARCHAR(50) DEFAULT 'OPEN',
    stipend               DOUBLE,
    duration_months       INT,
    mode                  VARCHAR(100),
    start_date            DATE,
    created_at            DATETIME,
    updated_at            DATETIME,
    CONSTRAINT fk_internship_company FOREIGN KEY (company_id) REFERENCES companies(id)
    );

-- ============================================================
-- Table: applications
-- ============================================================
CREATE TABLE IF NOT EXISTS applications (
                                            id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            student_id              BIGINT NOT NULL,
                                            job_posting_id          BIGINT,
                                            internship_posting_id   BIGINT,
                                            status                  VARCHAR(50) DEFAULT 'APPLIED',
    applied_on              DATE,
    created_at              DATETIME,
    updated_at              DATETIME,
    CONSTRAINT fk_app_student      FOREIGN KEY (student_id)            REFERENCES students(id),
    CONSTRAINT fk_app_job          FOREIGN KEY (job_posting_id)        REFERENCES job_postings(id),
    CONSTRAINT fk_app_internship   FOREIGN KEY (internship_posting_id) REFERENCES internship_postings(id)
    );

-- ============================================================
-- Table: interviews
-- ============================================================
CREATE TABLE IF NOT EXISTS interviews (
                                          id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          application_id  BIGINT NOT NULL,
                                          round_number    INT,
                                          scheduled_at    DATETIME,
                                          mode            VARCHAR(100),
    result          VARCHAR(50) DEFAULT 'PENDING',
    feedback        TEXT,
    created_at      DATETIME,
    updated_at      DATETIME,
    CONSTRAINT fk_interview_application FOREIGN KEY (application_id) REFERENCES applications(id)
    );

-- ============================================================
-- Sample Data
-- ============================================================

-- Companies
INSERT INTO companies (name, domain, email, location, description, created_at, updated_at) VALUES
                                                                                               ('TechCorp Solutions', 'Product',  'hr@techcorp.com', 'Hyderabad',  'Leading product-based software company', NOW(), NOW()),
                                                                                               ('Infosys',           'Service',  'hr@infosys.com',  'Bangalore',  'Global IT services and consulting',       NOW(), NOW()),
                                                                                               ('Amazon',            'Product',  'hr@amazon.com',   'Hyderabad',  'E-commerce and cloud computing giant',    NOW(), NOW()),
                                                                                               ('Wipro',             'Service',  'hr@wipro.com',    'Pune',       'IT services and consulting company',      NOW(), NOW()),
                                                                                               ('StartupX',          'Startup',  'hr@startupx.com', 'Hyderabad',  'Early stage product startup',             NOW(), NOW());

-- Students
INSERT INTO students (name, email, branch, cgpa, skills, resume_url, created_at, updated_at) VALUES
                                                                                                 ('Hima Reddy',   'hima@college.edu',   'CSE',  8.9, 'Java, Spring Boot, MySQL',          'https://drive.google.com/hima-resume',   NOW(), NOW()),
                                                                                                 ('Arjun Kumar',  'arjun@college.edu',  'CSE',  7.2, 'Python, Django, PostgreSQL',         'https://drive.google.com/arjun-resume',  NOW(), NOW()),
                                                                                                 ('Priya Sharma', 'priya@college.edu',  'ECE',  8.1, 'Embedded C, IoT, VLSI',             'https://drive.google.com/priya-resume',  NOW(), NOW()),
                                                                                                 ('Rahul Singh',  'rahul@college.edu',  'CSE',  6.5, 'JavaScript, React, Node.js',         'https://drive.google.com/rahul-resume',  NOW(), NOW()),
                                                                                                 ('Sneha Patel',  'sneha@college.edu',  'IT',   9.1, 'Java, AWS, Docker',                  'https://drive.google.com/sneha-resume',  NOW(), NOW()),
                                                                                                 ('Vikram Nair',  'vikram@college.edu', 'MECH', 7.8, 'AutoCAD, SolidWorks, MATLAB',        'https://drive.google.com/vikram-resume', NOW(), NOW()),
                                                                                                 ('Anjali Das',   'anjali@college.edu', 'ECE',  6.8, 'Signal Processing, Python, MATLAB',  'https://drive.google.com/anjali-resume', NOW(), NOW()),
                                                                                                 ('Karan Mehta',  'karan@college.edu',  'IT',   8.4, 'Spring Boot, Microservices, Kafka',  'https://drive.google.com/karan-resume',  NOW(), NOW());

-- Job Postings
INSERT INTO job_postings (company_id, title, description, required_skills, eligible_branches, min_cgpa, manual_review_enabled, status, ctc, job_type, location, deadline, created_at, updated_at) VALUES
                                                                                                                                                                                                      (1, 'Backend Developer',    'Build scalable REST APIs using Spring Boot',         'Java, Spring Boot, MySQL',        'CSE,IT',     7.0, FALSE, 'OPEN', 12.0, 'Full-time', 'Hyderabad', '2026-07-15', NOW(), NOW()),
                                                                                                                                                                                                      (2, 'Systems Engineer',     'Work on enterprise IT solutions and support',        'Java, SQL, Communication',        'CSE,IT,ECE', 6.0, TRUE,  'OPEN',  6.5, 'Full-time', 'Bangalore', '2026-07-20', NOW(), NOW()),
                                                                                                                                                                                                      (3, 'SDE-1',                'Work on Amazon core e-commerce platform',           'Data Structures, Java',           'CSE,IT',     8.0, FALSE, 'OPEN', 24.0, 'Full-time', 'Hyderabad', '2026-07-10', NOW(), NOW()),
                                                                                                                                                                                                      (5, 'Full Stack Developer', 'Build product features end to end',                 'React, Spring Boot, MySQL',       'CSE,IT,ECE', 6.5, TRUE,  'OPEN',  8.0, 'Full-time', 'Hyderabad', '2026-07-25', NOW(), NOW());

-- Internship Postings
INSERT INTO internship_postings (company_id, title, description, required_skills, eligible_branches, min_cgpa, manual_review_enabled, status, stipend, duration_months, mode, start_date, created_at, updated_at) VALUES
                                                                                                                                                                                                                      (1, 'Backend Intern', 'Work on REST APIs and database design',        'Java, Spring Boot', 'CSE,IT',     6.5, FALSE, 'OPEN', 20000, 6, 'Hybrid', '2026-07-01', NOW(), NOW()),
                                                                                                                                                                                                                      (3, 'Cloud Intern',   'Work on AWS infrastructure and DevOps',        'AWS, Linux, Docker','CSE,IT,ECE', 7.5, FALSE, 'OPEN', 25000, 3, 'Remote', '2026-08-01', NOW(), NOW()),
                                                                                                                                                                                                                      (4, 'IoT Intern',     'Work on embedded systems and IoT devices',     'Embedded C, IoT',   'ECE,EEE',    6.0, TRUE,  'OPEN', 12000, 6, 'Onsite', '2026-07-15', NOW(), NOW());
