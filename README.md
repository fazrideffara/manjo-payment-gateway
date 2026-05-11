# Manjo Payment Gateway (Backend) 🚀

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-green.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)

Sistem Payment Gateway tangguh yang dikembangkan oleh **PT Manjo Teknologi Indonesia**. Backend ini dirancang khusus untuk memproses transaksi **QRIS (MPM)** dengan standar keamanan tinggi menggunakan **HMAC-SHA256 Signature** dan kepatuhan penuh terhadap kode respons **SNAP BI**.

## 🌟 Fitur Utama

- **QRIS MPM Generation**: Pembuatan QR code dinamis yang kompatibel dengan standar nasional.
- **Transaction Query**: Pengecekan status transaksi real-time menggunakan `trx_id` atau `reference_number`.
- **Payment Webhook**: Notifikasi otomatis status pembayaran ke sistem Merchant/Partner.
- **Admin Management**: Dashboard backend untuk monitoring transaksi dan statistik volume harian.
- **Strict Security**: Validasi signature HMAC-SHA256 pada setiap request sensitif dan proteksi JWT untuk akses Admin.
- **SNAP BI Compliant**: Response code standar 7-digit (e.g., `4017300`, `2007300`).

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.4.0 (Java 21)
- **Database**: PostgreSQL 15+
- **Security**: Spring Security, JWT (Stateless), HMAC-SHA256
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Persistence**: Spring Data JPA with Hibernate

## 🚀 Instalasi & Persiapan

### 1. Prasyarat
- **Java 21** installed.
- **Maven 3.9+** installed.
- **PostgreSQL** server running.

### 2. Database Setup
Buat database di PostgreSQL:
```sql
CREATE DATABASE manjo_payment;
```

Konfigurasi koneksi di `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/manjo_payment
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Keamanan (Secret Keys)
Pastikan Secret Key sinkron dengan Frontend:
- **Signature Key**: `MANJO-SECRET-KEY-2026-PAYMENT-GATEWAY`
- **JWT Key**: `manjo-very-secret-key-for-jwt-2026-payment-gateway-must-be-long`

### 4. Menjalankan Aplikasi
```bash
./mvnw clean spring-boot:run
```
Aplikasi berjalan di: `http://localhost:8080`

## 📖 Dokumentasi API

Akses Swagger UI untuk interaksi langsung:
`http://localhost:8080/swagger-ui.html`

**Endpoint Utama:**
- `POST /api/v1/qr/generate` - Inisialisasi QRIS (Butuh X-Signature)
- `POST /api/v1/qr/payment` - Webhook Notifikasi Partner (Butuh X-Signature)
- `GET /api/v1/qr/query` - Cek Status Transaksi
- `POST /api/v1/auth/login` - Login Admin (Dapatkan JWT)

## 🔗 Hubungan dengan Frontend

Backend ini merupakan "Otak" bagi **Manjo Merchant Dashboard**.
- **Repo Frontend**: [manjo-frontend](https://github.com/fazrideffara/manjo-frontend)
- **CORS Config**: Sudah dikonfigurasi untuk menerima request dari `http://localhost:5173`.

---
© 2026 PT Manjo Teknologi Indonesia. All Rights Reserved.
