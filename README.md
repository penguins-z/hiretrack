# HireTrack API

A RESTful API for tracking job applications, built with Spring Boot and secured with JWT authentication.

## Tech Stack

- Java 21
- Spring Boot 3.5.11
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL
- Docker + Docker Compose
- Maven

## Features

- User registration and authentication with JWT tokens
- Track job applications with status management
- Organize applications by company
- Add notes and contacts to each application
- Paginated application listing
- Role-based access — users can only access their own data
- Global exception handling with consistent error responses
- Input validation
- API documentation with Swagger UI

## Project Structure
```
src/main/java/com/ferdin/hiretrack/
├── controller/     → REST endpoints
├── service/        → Business logic
├── repository/     → Database access
├── entity/         → JPA entities
├── dto/            → Request and response objects
├── security/       → JWT filter, Spring Security config
└── exception/      → Custom exceptions and global handler
```

## Running with Docker

The easiest way to run the project.

**Prerequisites:** Docker and Docker Compose installed.
```bash
git clone https://github.com/penguins-z/hiretrack.git
cd hiretrack
docker-compose up --build
```

The API will be available at `http://localhost:8080`

## Running Locally

**Prerequisites:** Java 21, Maven, PostgreSQL 17

1. Create a PostgreSQL database named `hiretrack`

2. Create `src/main/resources/application-local.properties`:
```properties
DB_USERNAME=postgres
DB_PASSWORD=yourpassword
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
```

3. Run:
```bash
./mvnw spring-boot:run
```

## API Documentation

Once running, visit `http://localhost:8080/swagger-ui.html` for interactive API documentation.

Use the Authorize button in Swagger UI to enter your JWT token after registering or logging in.

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register new user, returns JWT token |
| POST | /api/auth/login | Login, returns JWT token |

### Applications
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/applications | Create application |
| GET | /api/applications?page=0&size=10 | Get all applications (paginated) |
| GET | /api/applications/{id} | Get application by ID |
| PUT | /api/applications/{id} | Update application |
| PATCH | /api/applications/{id}/status | Update application status |
| DELETE | /api/applications/{id} | Delete application |

### Companies
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/companies | Create company |
| GET | /api/companies | Get all companies |
| GET | /api/companies/{id} | Get company by ID |
| PUT | /api/companies/{id} | Update company |
| DELETE | /api/companies/{id} | Delete company |

### Notes
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/applications/{id}/notes | Add note to application |
| GET | /api/applications/{id}/notes | Get all notes for application |
| DELETE | /api/applications/{id}/notes/{noteId} | Delete note |

### Contacts
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/applications/{id}/contacts | Add contact to application |
| GET | /api/applications/{id}/contacts | Get all contacts for application |
| DELETE | /api/applications/{id}/contacts/{contactId} | Delete contact |

## Application Status Flow
```
SAVED → APPLIED → INTERVIEW_SCHEDULED → INTERVIEW_DONE → OFFER_RECEIVED → ACCEPTED
                                                        → REJECTED
                                      → REJECTED
              → REJECTED
              → WITHDRAWN
```

## Security

All endpoints except `/api/auth/**` require a valid JWT token in the Authorization header:
```
Authorization: Bearer <token>
```

Users can only access their own data. Attempting to access another user's resources returns 404.

## Running Tests
```bash
./mvnw test
```