# MDA App — Spring Boot REST API with Role-Based Access and Audit Journal

A Spring Boot REST application for managing a product catalogue shared across services. It illustrates two authentication levels and **systematic journaling** of every action (traceability / non-repudiation) in a MySQL database.

## Features

- **Light authentication** for read access: consulting products requires only a user name and a service name.
- **Reinforced "RH" authentication** for write operations: adding, modifying or deleting a product requires an `authRH` header.
- **Audit journal:** every consultation, addition, modification and deletion is recorded (who, what action, which product, when).

## Tech stack

- Java 21
- Spring Boot 3.5.9 (Web, Data JPA)
- MySQL (`mysql-connector-j`)
- Maven (wrapper included)

## Project structure

```
src/main/java/com/miniprojet/mda_app/
├── MdaAppApplication.java
├── controller/AppController.java      # REST endpoints
├── service/AppService.java            # RH check + journaling
├── model/Produit.java                 # Product entity
├── model/Journal.java                 # Audit-log entity
└── repository/                        # Spring Data JPA repositories
```

## Prerequisites

- JDK 21
- MySQL running on `localhost:3306`

## Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_mda?createDatabaseIfNotExist=true
spring.datasource.username=<your-mysql-user>
spring.datasource.password=<your-mysql-password>
spring.jpa.hibernate.ddl-auto=update
```

The `db_mda` database and its tables (`produit`, `journal`) are created automatically on first start.

## Run

```bash
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows
```

The app starts on http://localhost:8080.

## API

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/` | none | Welcome page |
| GET | `/produits?nom=<user>&service=<service>` | light (query params) | List all products (logged as `CONSULTATION`) |
| POST | `/produits` | header `authRH` | Add a product |
| PUT | `/produits/{id}` | header `authRH` | Update a product |
| DELETE | `/produits/{id}` | header `authRH` | Delete a product |

Product JSON:

```json
{ "nom": "Capteur", "serviceOrigine": "Ingénierie", "prix": 120.5 }
```

### Example requests

```bash
# Consult
curl "http://localhost:8080/produits?nom=alice&service=Logistique"

# Add (reinforced authentication)
curl -X POST http://localhost:8080/produits \
  -H "Content-Type: application/json" \
  -H "authRH: <rh-password>" \
  -d '{"nom":"Capteur","serviceOrigine":"Ingénierie","prix":120.5}'
```

## Known limitations

- The RH password is currently hard-coded in `AppService.verifierRH`; move it to an environment variable or use Spring Security.
- The database password in `application.properties` should not be committed. Use environment variables or a local, git-ignored config file.
- The light authentication does not verify identity; it only records the name given.
- Write endpoints return plain strings (including on auth failure) with HTTP 200 instead of proper 401/403 codes.
