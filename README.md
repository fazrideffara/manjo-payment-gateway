# Manjo Payment Gateway (Backend) 🚀

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-green.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)

Sistem Payment Gateway tangguh yang dikembangkan oleh **PT Manjo Teknologi Indonesia**. Backend ini dirancang untuk memproses transaksi pembayaran (QRIS, Bank Transfer, CC) dengan standar keamanan tinggi menggunakan **HMAC-SHA256 Signature** dan kepatuhan terhadap kode respons **SNAP BI**.

## 🌟 Fitur Utama

- **QRIS MPM Generation**: Pembuatan QR code dinamis untuk pembayaran.
- **Transaction Query**: Pengecekan status transaksi real-time.
- **Payment Notification (Webhook)**: Sistem callback otomatis ke merchant/partner.
- **QR Cancellation**: Fitur pembatalan transaksi yang masih pending.
- **Signature Security**: Validasi integritas data menggunakan HMAC-SHA256.
- **Standardized Response**: Menggunakan kode respons 7-digit (e.g., `2004700`, `2005100`).

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.4
- **Database**: PostgreSQL
- **Security**: Spring Security, JWT, HMAC-SHA256
- **Documentation**: Swagger/OpenAPI (SpringDoc)
- **Persistence**: Spring Data JPA (Hibernate)

## 🚀 Instalasi & Persiapan

### 1. Prasyarat
- **Java 21** atau lebih baru.
- **Maven** 3.9+.
- **PostgreSQL** server berjalan secara lokal atau remote.

### 2. Database Setup
Buat database baru di PostgreSQL:
```sql
CREATE DATABASE manjo_payment;
```

Sesuaikan konfigurasi di `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/manjo_payment
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Menjalankan Aplikasi
Clone repository ini dan jalankan perintah:
```bash
./mvnw clean spring-boot:run
```
Aplikasi akan berjalan di: `http://localhost:8080`

## 📖 Dokumentasi API

Akses Swagger UI untuk melihat detail endpoint:
`http://localhost:8080/swagger-ui.html`

### Endpoint Utama (Original Requirements)
- `POST /api/v1/qr/generate` - Buat QR
- `GET /api/v1/qr/query` - Cek Status
- `POST /api/v1/qr/payment` - Callback Notifikasi
- `POST /api/v1/qr/cancel` - Batal Transaksi

## 🔗 Hubungan dengan Frontend

Backend ini dirancang untuk bekerja secara mulus dengan **Manjo Merchant Dashboard**.
- **Repo Frontend**: [manjo-frontend](https://github.com/fazrideffara/manjo-frontend)
- **Base URL Frontend**: `http://localhost:5173`

---
© 2026 PT Manjo Teknologi Indonesia. All Rights Reserved.
