# System Zarządzania Projektami – Opis Projektu

## Cel projektu

Projekt ćwiczeniowy mający na celu naukę fullstack developmentu w technologiach Java / Spring Boot / Angular / PostgreSQL. Aplikacja to system zarządzania projektami i zadaniami w stylu Jira/Trello – umożliwia tworzenie projektów, sprintów, zadań oraz współpracę zespołową w czasie rzeczywistym.

---

## Stos technologiczny

### Backend
- **Java 21**
- **Spring Boot 3.5.13**
- **Spring Security** – autoryzacja i uwierzytelnianie (JWT)
- **Spring Data JPA + Hibernate** – warstwa dostępu do danych
- **PostgreSQL 16** – relacyjna baza danych
- **Liquibase** – migracje schematu bazy danych
- **MapStruct** – mapowanie encji ↔ DTO
- **OpenAPI Generator** – generowanie interfejsów kontrolerów i DTO z plików YAML (API First)
- **Springdoc OpenAPI** – Swagger UI
- **WebSocket (STOMP)** – komunikacja w czasie rzeczywistym
- **Apache POI** – import/eksport plików Excel
- **iText / JasperReports** – generowanie raportów PDF
- **jjwt 0.12.6** – obsługa tokenów JWT

### Frontend
- **Angular 17+**
- **Angular Material**
- **RxJS**
- **STOMP.js** – obsługa WebSocket po stronie klienta
- **OpenAPI Generator** – generowanie klienta HTTP z plików YAML

### Infrastruktura
- **Docker + Docker Compose** – konteneryzacja (PostgreSQL, backend, frontend)
- **Testcontainers** – testy integracyjne z prawdziwą bazą PostgreSQL
- **JUnit 5 + Mockito** – testy jednostkowe

---

## Architektura

Projekt zbudowany jako **modularny monolit** z podejściem **API First (OpenAPI)**. Składa się z czterech modułów Maven:

### Moduły

| Moduł | Zawartość |
|---|---|
| `task-managament-api` | Pliki YAML (OpenAPI spec), generowanie interfejsów i DTO |
| `task-management-commons` | Spring Security, JWT, obsługa wyjątków, klasy współdzielone |
| `task-management-domain` | Encje, repozytoria, serwisy, fasady, DTO, MapStruct mappery |
| `task-management-web-server` | Kontrolery REST, konfiguracja aplikacji, punkt startowy |

### Zasady modularnego monolitu

- Każdy moduł biznesowy w `domain` ma własne podpakiety: `user/`, `project/`, `sprint/`, `task/`, `comment/`, `notification/`
- Moduły komunikują się przez **fasady** (synchronicznie) lub **Spring Events** (asynchronicznie)
- `server` importuje `api` jako zależność i implementuje wygenerowane interfejsy kontrolerów
- Wygenerowany kod z OpenAPI nigdy nie jest edytowany ręcznie

### Komunikacja między modułami

- **Fasada (synchroniczna)** – gdy moduł potrzebuje danych z innego modułu natychmiast (np. walidacja czy użytkownik należy do projektu)
- **Spring Events (asynchroniczna)** – gdy moduł reaguje na zdarzenie bez potrzeby natychmiastowej odpowiedzi (np. `TaskAssignedEvent` → tworzy powiadomienie)

---

## Model danych (główne encje)

```
users
projects
project_members     ← rola użytkownika w projekcie (OWNER/MEMBER/VIEWER)
sprints
tasks
comments
notifications
```

Wszystkie encje dziedziczą z `BaseEntity` która zawiera:
- `id` (UUID)
- `version` (Optimistic Locking)
- `createdAt`, `updatedAt` (JPA Auditing)
- `createdBy` (nazwa zalogowanego użytkownika)

---

## Funkcjonalności

### Faza 1 – Fundament (CRUD + Auth)
- Rejestracja i logowanie użytkowników (JWT)
- Zarządzanie projektami z rolami (OWNER, MEMBER, VIEWER)
- Zarządzanie sprintami (PLANNED, ACTIVE, COMPLETED)
- Zarządzanie zadaniami (statusy, priorytety, przypisanie)
- Komentarze do zadań
- Walidacja requestów (Bean Validation)
- Globalna obsługa błędów (`@ControllerAdvice`)
- Swagger UI

### Faza 2 – Real-time
- Powiadomienia live przez WebSocket (STOMP)
- Live aktualizacja tablicy Kanban
- Spring Events: `TaskStatusChangedEvent`, `TaskAssignedEvent`

### Faza 3 – Import / Eksport
- Import zadań z pliku `.xlsx` (Apache POI)
- Eksport raportu sprintu do `.pdf`
- Eksport zadań do `.xlsx`
- Walidacja importowanego pliku z raportem błędów

### Faza 4 – Wielowątkowość
- Generowanie PDF/XLSX w tle (`@Async`)
- Powiadomienie użytkownika gdy plik jest gotowy
- Import dużego pliku Excel partiami (Spring Batch lub własny executor)
- `@Scheduled` – codzienny digest e-mail

### Faza 5 – Jakość i infrastruktura
- Docker Compose (PostgreSQL + backend + frontend)
- Testy integracyjne (Testcontainers)
- Testy jednostkowe (JUnit 5, Mockito)
- Paginacja i filtrowanie list

---

## Widoki Angular

| Widok | Opis |
|---|---|
| Login / Register | Formularze reaktywne, walidacja |
| Dashboard | Lista projektów użytkownika |
| Projekt – Board | Kanban, drag & drop (CDK), live updates |
| Projekt – Backlog | Lista zadań z filtrowaniem i paginacją |
| Projekt – Sprinty | Zarządzanie sprintami |
| Zadanie – Szczegóły | Edycja, komentarze, historia zmian |
| Powiadomienia | Panel z live powiadomieniami |
| Import / Eksport | Upload Excel, pobieranie PDF/XLSX |

---

## Czego się nauczysz

| Obszar | Technologie i koncepty |
|---|---|
| Architektura | Modularny monolit, Fasada, Spring Events, API First |
| Backend | JWT, Spring Security, JPA relations, Optimistic Locking, N+1 problem |
| Mapowanie | MapStruct, DTO pattern, OpenAPI Generator |
| Baza danych | Liquibase, migracje, Hibernate Auditing |
| Real-time | WebSocket, STOMP, RxJS |
| Pliki | Apache POI, iText, streaming response |
| Wielowątkowość | @Async, ThreadPoolTaskExecutor, Spring Batch, @Scheduled |
| Testy | Testcontainers, JUnit 5, Mockito, @WebMvcTest |
| DevOps | Docker, Docker Compose |
