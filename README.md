# Employee Management System

## Layihə haqqında

Employee Management System — Spring Boot əsasında hazırlanmış backend tətbiqidir. Sistem işçilərin idarə olunması, istifadəçi autentifikasiyası, rol əsaslı icazələndirmə (authorization) və davamiyyətin (attendance) izlənməsi üçün nəzərdə tutulmuşdur.

Layihə real backend arxitektura prinsiplərinə uyğun şəkildə qurulmuşdur və DTO pattern, caching, scheduling və security kimi mexanizmləri əhatə edir.

Bu layihənin məqsədi enterprise səviyyəyə yaxın backend sistemin qurulmasını nümayiş etdirməkdir.

---

## İstifadə olunan texnologiyalar

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* MySQL
* Redis (Cache)
* MapStruct
* Lombok
* Docker
* Maven

---

## Funksionallıqlar

* İstifadəçi autentifikasiyası (Authentication)
* Rol əsaslı icazələndirmə (Authorization)
* İşçi idarəetməsi (Employee management)
* Daxilolma / çıxış vaxtlarının qeydiyyatı (DailyCheck)
* Gecikmə vaxtının avtomatik hesablanması
* Bayram günlərinin avtomatik nəzərə alınması
* Redis ilə caching
* Planlaşdırılmış job-lar (Scheduling)
* DTO və Mapper strukturu
* Global exception handling
* Docker ilə konteynerləşdirmə

---

## Arxitektura

Layihədə layered architecture istifadə olunmuşdur:

Controller → Service → Repository → Database

Entity və API modelləri bir-birindən DTO pattern vasitəsilə ayrılmışdır.
MapStruct obyektlərin map edilməsi üçün istifadə olunur.

Spring Security autentifikasiya və avtorizasiya üçün istifadə olunur.
Redis performansı artırmaq üçün caching mexanizmi kimi istifadə olunur.

---

## Biznes məntiqi

Sistem aşağıdakı ssenarini dəstəkləyir:

* İşçi sistemə daxil olur
* İşçi giriş və çıxış vaxtını qeyd edir
* Sistem gecikmə vaxtını hesablayır
* Bayram günləri avtomatik nəzərə alınır
* Rol əsaslı icazələr məlumatların qorunmasını təmin edir

---

## Project strukturu

```
controller
service
repository
entity
dto
mapper
security
config
exception
util
```

---

## Layihəni işə salmaq

### Repository-ni klonla

```
git clone https://github.com/zeynalovruslan/employee_managment_system
```

### Build et

```
mvn clean install
```

### Run et

```
mvn spring-boot:run
```

---

## Docker ilə işə salmaq

```
docker-compose up --build
```

---

## Database konfiqurasiyası

Database ayarları aşağıdakı faylda yerləşir:

```
application.yml
```

---

## Cache

Layihədə Redis cache istifadə olunur.
Bu tez-tez istifadə olunan məlumatların daha sürətli əldə edilməsini təmin edir.

---

## Security

Spring Security istifadə olunaraq:

* Authentication
* Role-based authorization
* Method-level security

implementasiya edilmişdir.

---

## Müəllif

Ruslan Zeynalov

Junior Java Backend Developer
