📘 Hotel Management System – Backend (Spring Boot + JWT + PostgreSQL)

A complete Hotel Room Booking Backend System designed using Spring Boot, Spring Security 7, JWT, JPA/Hibernate, and PostgreSQL.

This project supports:

Multi-Hotel Management

Room Management

Booking with Date Overlap Check

JWT-based Authentication

Role-based Authorization (ADMIN + CUSTOMER)

Real-world REST API architecture

Fully modular layered structure

🚀 Features
🔐 Authentication & Authorization

Register / Login using JWT

Secure APIs using Spring Security

Roles → ADMIN, CUSTOMER

Customers can browse & book rooms

Admin can manage hotels/rooms/bookings

🏨 Hotel Module

Add Hotel (Admin)

Update Hotel (Admin)

Delete Hotel (Admin)

Get All Hotels

Search Hotels by City

Get Hotel-wise Available Rooms

🚪 Room Module

Add Room (Admin)

Update Room (Admin)

Delete Room (Admin)

Get room by ID

Get rooms of a hotel

Get available rooms by date

Pagination + Sorting

📅 Booking Module

Book a room

Prevent Overlapping Bookings

Cancel Booking

Get User Bookings

Get All Bookings (Admin)

Check real-time availability

🧱 Project Architecture (Layered)
Controller → Service → DAO → Entity → Database
               ↓
             DTOs
               ↓
        Exception Handling

🗄️ Database Design
Entities:

User

Hotel

Room

Booking

Relationships:
Hotel (1) ---- (N) Room
Room (1)  ---- (N) Booking
User (1)  ---- (N) Booking

🛠️ Tech Stack
Layer	Technology
Backend	Spring Boot 4
Security	Spring Security 7 + JWT
Database	PostgreSQL
ORM	Hibernate / JPA
Build Tool	Maven
Language	Java 17
Tools	Postman, Git, GitHub
📌 API Endpoints Overview
🔐 Authentication
METHOD	ENDPOINT	DESCRIPTION
POST	/api/auth/register	Register new user
POST	/api/auth/login	Login & get JWT token
🏨 Hotels
METHOD	ENDPOINT	ACCESS
POST	/api/hotels	ADMIN
GET	/api/hotels	ALL
GET	/api/hotels/{id}	ALL
PUT	/api/hotels/{id}	ADMIN
DELETE	/api/hotels/{id}	ADMIN
GET	/api/hotels/search?city={city}	ALL
GET	/api/hotels/{id}/available?checkIn=&checkOut=	ALL
🚪 Rooms
METHOD	ENDPOINT	ACCESS
POST	/api/rooms	ADMIN
GET	/api/rooms	ALL
GET	/api/rooms/{id}	ALL
PUT	/api/rooms/{id}	ADMIN
DELETE	/api/rooms/{id}	ADMIN
GET	/api/rooms/hotel/{hotelId}	ALL
GET	/api/rooms/available?checkIn=&checkOut=	ALL
📅 Bookings
METHOD	ENDPOINT	ACCESS
POST	/api/bookings	USER+ADMIN
GET	/api/bookings	ADMIN
GET	/api/bookings/user/{userId}	USER+ADMIN
DELETE	/api/bookings/{id}	USER+ADMIN
🧪 Sample JSON
Add Hotel
{
  "name": "Taj Hotel",
  "city": "Hyderabad",
  "address": "Banjara Hills"
}

Add Room
{
  "roomNumber": "302",
  "type": "DELUXE",
  "pricePerNight": 3500,
  "available": true,
  "hotelId": 1
}

Book Room
{
  "userId": 2,
  "roomId": 1,
  "checkInDate": "2025-01-15",
  "checkOutDate": "2025-01-17"
}

⚙️ Setup Instructions
1️⃣ Clone Repo
git clone https://github.com/<your-username>/hotel-management-system.git
cd hotel-management-system

2️⃣ Configure PostgreSQL

Create database:

CREATE DATABASE hotelbooking;

3️⃣ Add application.properties (not included for security)
spring.datasource.url=jdbc:postgresql://localhost:5432/hotelbooking
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
jwt.secret=YOUR_SECRET

4️⃣ Run Application
mvn spring-boot:run

📘 Author

Raghu Reddy
Java Full Stack Developer
Spring Boot | Hibernate | PostgreSQL | React | Git | REST APIs
