# Plan Realizacji – System Zarządzania Projektami

## Legenda
- ✅ Zrobione
- 🔄 W trakcie
- ⬜ Do zrobienia

---

## Setup i infrastruktura

- ✅ Utworzenie projektu wielomodułowego Maven
- ✅ Konfiguracja parent `pom.xml` (`dependencyManagement`, wersje bibliotek)
- ✅ Moduł `task-managament-api` – OpenAPI Generator, `api.yaml`
- ✅ Moduł `task-management-commons` – Spring Security, JWT, obsługa wyjątków
- ✅ Moduł `task-management-domain` – JPA, MapStruct, encje
- ✅ Moduł `task-management-web-server` – kontrolery, Swagger, punkt startowy
- ✅ `docker-compose.yml` – PostgreSQL 16 z healthcheck i named volume
- ✅ `application.yml` – konfiguracja datasource, JPA, Liquibase
- ✅ `application-test.yml` – Testcontainers dla testów integracyjnych
- ✅ `JpaConfig` + `SpringSecurityAuditorAware` – JPA Auditing w module `server`
- ✅ `BaseEntity` – wspólna klasa bazowa dla encji (UUID, version, auditing)

---

## Faza 1 – Fundament (CRUD + Auth)

### Baza danych
- ⬜ Liquibase – konfiguracja `db.changelog-master.yaml`
- ⬜ Liquibase – changeset: tabela `users`
- ⬜ Liquibase – changeset: tabela `projects`
- ⬜ Liquibase – changeset: tabela `project_members`
- ⬜ Liquibase – changeset: tabela `sprints`
- ⬜ Liquibase – changeset: tabela `tasks`
- ⬜ Liquibase – changeset: tabela `comments`
- ⬜ Liquibase – changeset: tabela `notifications`

### Encje i repozytoria (moduł `domain`)
- ⬜ Encja `User`
- ⬜ Encja `Project`
- ⬜ Encja `ProjectMember` (rola: OWNER/MEMBER/VIEWER)
- ⬜ Encja `Sprint` (status: PLANNED/ACTIVE/COMPLETED)
- ⬜ Encja `Task` (status: TODO/IN_PROGRESS/IN_REVIEW/DONE, priorytet: LOW/MEDIUM/HIGH/CRITICAL)
- ⬜ Encja `Comment`
- ⬜ Encja `Notification`
- ⬜ `UserRepository`
- ⬜ `ProjectRepository`
- ⬜ `SprintRepository`
- ⬜ `TaskRepository`
- ⬜ `CommentRepository`

### DTO i MapStruct (moduł `domain`)
- ⬜ DTO dla `User` (RegisterRequest, LoginRequest, UserResponse)
- ⬜ DTO dla `Project` (CreateRequest, UpdateRequest, Response, SummaryResponse)
- ⬜ DTO dla `Sprint` (CreateRequest, Response)
- ⬜ DTO dla `Task` (CreateRequest, UpdateRequest, Response, SummaryResponse)
- ⬜ DTO dla `Comment` (CreateRequest, Response)
- ⬜ MapStruct mappery dla każdej encji

### OpenAPI spec (moduł `api`)
- ⬜ `components.yaml` – współdzielone schematy (ErrorResponse, PageResponse)
- ⬜ Endpointy Auth (`/api/auth/register`, `/api/auth/login`)
- ⬜ Endpointy Projects (`/api/projects/**`)
- ⬜ Endpointy Sprints (`/api/projects/{id}/sprints/**`)
- ⬜ Endpointy Tasks (`/api/tasks/**`)
- ⬜ Endpointy Comments (`/api/tasks/{id}/comments/**`)

### Serwisy i fasady (moduł `domain`)
- ⬜ `UserService` + `UserFacade`
- ⬜ `ProjectService` + `ProjectFacade`
- ⬜ `SprintService`
- ⬜ `TaskService` + `TaskFacade`
- ⬜ `CommentService`

### Spring Security + JWT (moduł `commons`)
- ⬜ `JwtService` – generowanie i walidacja tokenów
- ⬜ `JwtAuthenticationFilter` – filtr JWT (`OncePerRequestFilter`)
- ⬜ `UserDetailsServiceImpl` – ładowanie użytkownika z bazy
- ⬜ `SecurityConfig` – konfiguracja `SecurityFilterChain`

### Kontrolery (moduł `server`)
- ⬜ `AuthController` – rejestracja i logowanie
- ⬜ `ProjectController` – implementacja wygenerowanego interfejsu
- ⬜ `SprintController`
- ⬜ `TaskController`
- ⬜ `CommentController`
- ⬜ `GlobalExceptionHandler` – `@ControllerAdvice`

### Testy
- ⬜ Testy jednostkowe `UserService`
- ⬜ Testy jednostkowe `ProjectService`
- ⬜ Testy jednostkowe `TaskService`
- ⬜ Testy integracyjne `AuthController` (Testcontainers)
- ⬜ Testy integracyjne `ProjectController` (Testcontainers)

---

## Faza 2 – Real-time (WebSockety + Spring Events)

- ⬜ Konfiguracja WebSocket (STOMP)
- ⬜ `NotificationService` – tworzenie i wysyłanie powiadomień
- ⬜ Spring Event: `TaskStatusChangedEvent`
- ⬜ Spring Event: `TaskAssignedEvent`
- ⬜ `NotificationController` – endpoint WebSocket
- ⬜ Angular: konfiguracja STOMP.js
- ⬜ Angular: live aktualizacja tablicy Kanban

---

## Faza 3 – Import / Eksport

- ⬜ Import zadań z `.xlsx` (Apache POI)
- ⬜ Walidacja importowanego pliku + raport błędów
- ⬜ Eksport raportu sprintu do `.pdf`
- ⬜ Eksport zadań do `.xlsx`
- ⬜ Endpoint upload/download plików

---

## Faza 4 – Wielowątkowość

- ⬜ Konfiguracja `ThreadPoolTaskExecutor`
- ⬜ `@Async` – generowanie PDF/XLSX w tle
- ⬜ Powiadomienie użytkownika gdy plik gotowy (WebSocket)
- ⬜ Import dużego pliku partiami (Spring Batch lub własny executor)
- ⬜ `@Scheduled` – codzienny digest e-mail

---

## Faza 5 – Jakość i infrastruktura

- ⬜ Docker Compose – dodanie backendu
- ⬜ Docker Compose – dodanie frontendu Angular
- ⬜ Testy integracyjne (Testcontainers) – pełne pokrycie
- ⬜ Paginacja i filtrowanie list (`Pageable`)
- ⬜ Liquibase – migracje dla danych testowych (seed data)

---

## Angular – Frontend

- ⬜ Setup projektu Angular + Angular Material
- ⬜ Generowanie klienta HTTP z OpenAPI spec
- ⬜ HTTP Interceptor – dodawanie JWT do requestów
- ⬜ Auth Guard – ochrona tras
- ⬜ Widok Login / Register
- ⬜ Widok Dashboard (lista projektów)
- ⬜ Widok Board (Kanban, drag & drop)
- ⬜ Widok Backlog (lista zadań, filtrowanie)
- ⬜ Widok Sprinty
- ⬜ Widok Zadanie – szczegóły i komentarze
- ⬜ Panel powiadomień (WebSocket)
- ⬜ Widok Import / Eksport

---

## Następny krok

🔄 **Faza 1** – Encje i Liquibase:
1. Napisać encję `User`
2. Skonfigurować Liquibase
3. Napisać pierwszy changeset (`users` table)
