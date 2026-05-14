# Plan Projektu - System Zarzadzania Projektami

## Stos technologiczny
- Backend: Java 21, Spring Boot 3, Spring Security, Spring Data JPA, Hibernate, PostgreSQL, WebSocket (STOMP), Apache POI, iText/JasperReports, MapStruct
- Frontend: Angular 17+, Angular Material, RxJS, STOMP.js
- Infrastruktura: Docker, Docker Compose, Liquibase, Swagger/OpenAPI

## Schemat bazy danych (glowne encje)
- User -> Project (many-to-many przez ProjectMember)
- Project -> Sprint (one-to-many)
- Sprint -> Task (one-to-many)
- Task -> Comment (one-to-many)
- Task -> Attachment (one-to-many)
- User -> Notification (one-to-many)

## Fazy projektu

### Faza 1 - Fundament (CRUD + Auth)
- Rejestracja i logowanie (JWT + Spring Security)
- Zarzadzanie projektami (tworzenie, edycja, usuwanie, lista)
- Zarzadzanie sprintami w ramach projektu
- Zarzadzanie zadaniami (CRUD, przypisanie do uzytkownika, status, priorytet)
- Komentarze do zadan
- Role w projekcie (OWNER, MEMBER, VIEWER)
- Swagger / OpenAPI
- Liquibase - migracje schematu

### Faza 2 - Real-time (WebSockety + Spring Events)
- Powiadomienia live gdy zmieni sie status zadania
- Powiadomienie gdy zostaniesz przypisany do zadania
- Live aktualizacja widoku tablicy Kanban (bez odswiezania strony)
- Spring Events wewnetrznie (np. TaskAssignedEvent -> tworzy powiadomienie)

### Faza 3 - Import / Eksport
- Import zadan z pliku .xlsx (Apache POI)
- Eksport raportu sprintu do .pdf (lista zadan, statusy, assignee)
- Eksport zadan projektu do .xlsx
- Walidacja importowanego pliku (bledne wiersze -> raport bledow)

### Faza 4 - Wielowatkowosc
- Generowanie PDF/XLSX w tle (@Async) - uzytkownik dostaje powiadomienie gdy plik gotowy
- Import duzego pliku Excel przetwarzany partiami (Spring Batch lub wlasny executor)
- @Scheduled - codzienny digest e-mail z podsumowaniem zadan

### Faza 5 - Jakosc i infrastruktura
- Docker + Docker Compose (PostgreSQL + backend + frontend)
- Testy integracyjne (Testcontainers + PostgreSQL)
- Testy jednostkowe (JUnit 5, Mockito)
- Obsluga bledow (@ControllerAdvice, wlasne wyjatki)
- Paginacja i filtrowanie list

## Widoki Angular (glowne)
- Login / Register
- Auth Dashboard
- Lista projektow uzytkownika
- Projekt - Board (Kanban z zadaniami, live updates)
- Projekt - Backlog (lista zadan z filtrowaniem)
- Projekt - Sprinty (zarzadzanie sprintami)
- Zadanie - Szczegoly (edycja, komentarze, historia zmian)
- Powiadomienia (panel z live powiadomieniami)
- Import / Eksport (upload Excel, pobieranie raportow)

## Czego sie nauczysz w kazdej fazie

| Faza | Kluczowe koncepty |
| --- | --- |
| 1 | JWT, Spring Security, JPA relations, DTO + MapStruct, Liquibase |
| 2 | WebSocket + STOMP, Spring Events, RxJS + WS w Angular |
| 3 | Apache POI, iText, walidacja plikow, streaming response |
| 4 | @Async, ThreadPoolTaskExecutor, Spring Batch basics, @Scheduled |
| 5 | Testcontainers, Docker Compose, global exception handling |

## User Stories - Faza 1

### Autoryzacja

**US-001 - Rejestracja**

Jako nowy uzytkownik chce zalozyc konto podajac imie, nazwisko, email i haslo, zeby moc korzystac z systemu.

Kryteria akceptacji:
- Email musi byc unikalny
- Haslo minimum 8 znakow
- Po rejestracji uzytkownik dostaje JWT i jest zalogowany

**US-002 - Logowanie**

Jako uzytkownik chce sie zalogowac emailem i haslem, zeby uzyskac dostep do swoich projektow.

Kryteria akceptacji:
- Przy blednych danych - komunikat o bledzie
- Po zalogowaniu token JWT zapisany w localStorage
- Token wygasa po 24h

### Projekty

**US-003 - Tworzenie projektu**

Jako zalogowany uzytkownik chce stworzyc nowy projekt podajac nazwe i opis, zeby moc zaczac organizowac prace.

Kryteria akceptacji:
- Tworca projektu automatycznie dostaje role OWNER
- Nazwa projektu jest wymagana, max 100 znakow
- Nowy projekt pojawia sie na dashboardzie

**US-004 - Zapraszanie czlonkow**

Jako OWNER projektu chce zaprosic uzytkownika do projektu podajac jego email i wybierajac role, zeby mogl wspolpracowac.

Kryteria akceptacji:
- Role do wyboru: MEMBER, VIEWER
- Jesli email nie istnieje w systemie - stosowny blad
- Zaproszony uzytkownik widzi projekt na dashboardzie

### Sprinty

**US-005 - Tworzenie sprintu**

Jako OWNER lub MEMBER chce stworzyc sprint z nazwa i datami (start/koniec), zeby zaplanowac iteracje pracy.

Kryteria akceptacji:
- Data konca musi byc po dacie startu
- Sprint nalezy do konkretnego projektu
- Status sprintu: PLANNED, ACTIVE, COMPLETED (tylko jeden ACTIVE naraz)

### Zadania

**US-006 - Tworzenie zadania**

Jako MEMBER projektu chce dodac zadanie do sprintu lub backlogu z tytulem, opisem, priorytetem i przypisanym uzytkownikiem.

Kryteria akceptacji:
- Priorytet: LOW, MEDIUM, HIGH, CRITICAL
- Status domyslny: TODO
- Zadanie mozna zostawic bez przypisanego uzytkownika

**US-007 - Zmiana statusu zadania**

Jako MEMBER chce zmienic status zadania (TODO -> IN PROGRESS -> IN REVIEW -> DONE), zeby odzwierciedlac postep prac.

Kryteria akceptacji:
- Zmiana statusu mozliwa przez przeciaganie karty (Kanban) lub dropdown
- VIEWER nie moze zmieniac statusow
- Zmiana jest natychmiast widoczna (to sie rozbuduje w Fazie 2)

**US-008 - Komentarze**

Jako uzytkownik projektu chce dodawac komentarze do zadan, zeby komunikowac sie z zespolem w kontekscie zadania.

Kryteria akceptacji:
- Komentarz ma autora i timestamp
- Mozna usunac tylko swoj komentarz
- Komentarze wyswietlane chronologicznie

