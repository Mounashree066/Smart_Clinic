# Smart Clinic

A full-stack clinic appointment management system developed using Spring Boot and React.

## Project Overview

Smart Clinic is a web application that allows users to register, log in, and manage clinic appointments through an easy-to-use interface.

The project consists of:

* Frontend: React.js
* Backend: Spring Boot
* Database: MySQL
* Authentication: JWT (JSON Web Token)

## Features

### User Features

* User Registration
* User Login
* Secure Authentication using JWT
* View Appointments
* Book Appointments
* Manage Appointment Details

### Admin Features

* View All Appointments
* Manage Users
* Update Appointment Status

## Technology Stack

### Frontend

* React.js
* HTML
* CSS
* JavaScript
* Axios

### Backend

* Spring Boot
* Spring Security
* Spring Data JPA
* JWT Authentication

### Database

* MySQL

## Project Structure

Smart_Clinic/

├── appointment-ui/        # React Frontend

└── appointment_1/         # Spring Boot Backend

## Installation

### Backend Setup

1. Navigate to backend folder:

```bash
cd appointment_1
```

2. Configure database credentials in:

```properties
application.properties
```

3. Run the application:

```bash
./gradlew bootRun
```

or

```bash
gradlew bootRun
```

Backend will start on:

```text
http://localhost:8080
```

### Frontend Setup

1. Navigate to frontend folder:

```bash
cd appointment-ui
```

2. Install dependencies:

```bash
npm install
```

3. Start the application:

```bash
npm start
```

Frontend will start on:

```text
http://localhost:3000
```

## API Testing

You can test APIs using:

* Postman
* Swagger (if configured)

## Future Enhancements

* Doctor Management
* Online Payments
* Appointment Notifications
* Email Verification
* Dashboard Analytics

