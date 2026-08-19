# CLAUDE.md — edatos-publisher

## Project overview

**edatos-publisher** is the ISTAC/eDatos open-data publication application. Its responsibilities are:
- Consuming Kafka events from `metamac-statistical-resources`, `metamac-statistical-operations`, and `metamac-srm` (datasets, codelists, concept schemes, organisation schemes, category schemes).
- Publishing the received resources to a CKAN open-data portal via its REST API.
- Tracking published external items (`ExternalCkanItemEntity`) and package relationships (`CkanPackageRelationshipEntity`).
- Sending failure notifications via the METAMAC notifications REST API.
- Providing an Angular 9 internal administration UI (`edatos-publisher-internal-js`).

**Version**: 2.2.1-SNAPSHOT
**GroupId**: `es.gobcan.istac.edatos-publisher`

## Modules

| Module | Packaging | Description |
|---|---|---|
| `edatos-publisher` (root) | POM | Parent POM — dependency management, version properties, Maven profiles |
| `edatos-publisher-core` | JAR | Domain entities, repositories, services, configuration, Liquibase migrations, i18n |
| `edatos-publisher-rest-common` | JAR | REST DTOs, MapStruct mappers, pagination utilities, JSON views |
| `edatos-publisher-internal-rest` | — | REST controller test classes only (no production code) |
| `edatos-publisher-internal-web` | WAR | Spring Boot application entry point, Kafka consumers, CKAN service, health indicators |
| `edatos-publisher-internal-js` | JAR | Angular 9 frontend (built via frontend-maven-plugin, served as static resources) |

### Dependency flow
```
core (domain + services)
  ↓
rest-common (DTOs + mappers)
  ↓
internal-web (Spring Boot app + Kafka consumers + CKAN)
  ↓
internal-js (Angular UI, packaged into internal-web)
```

## Technology stack

- **Java 8** — lambdas, streams, Optional, `java.time` are **allowed**
- **Spring Boot 1.5.4.RELEASE** — annotation-based configuration (`@Configuration`, `@Component`, `@Service`)
- **Hibernate 5.2.8.Final + JPA** — ORM with native PostgreSQL sequences
- **Liquibase 3.10.1** — database schema migrations
- **MapStruct 1.4.2.Final** — compile-time object mapping (entity ↔ DTO)
- **Apache CXF 3.3.5** — JAX-RS REST client for CKAN API calls
- **Apache Kafka + Confluent 6.1.1 + Avro** — event streaming (consumer only)
- **Spring Kafka 1.3.11.RELEASE** — Kafka consumer infrastructure
- **Angular 9** — internal web UI (PrimeNG, Nebular, ngx-translate)
- **Thymeleaf 3.0.6** — server-side template engine
- **Jackson 2.12.2** — JSON serialization
- **PostgreSQL 42.3.1** — JDBC driver
- **H2 1.4.200** — in-memory database for tests
- **Quartz 2.3.2** — job scheduling
- **JWT (jjwt 0.7.0) + CAS** — authentication
- **JHipster 1.1.8** — application scaffolding and infrastructure
- **SLF4J + Logback** — logging
- **Node v12.14.0** — frontend build toolchain

## Package naming

Single root package: `es.gobcan.istac.edatos.publisher`

| Package | Content |
|---|---|
| `*.core.domain.*` | JPA entities, enumerations, value objects, interfaces |
| `*.core.repository.*` | Spring Data JPA repositories |
| `*.core.service.*` | Business services (MetadataConfigurationService, ExternalCkanItemService, etc.) |
| `*.core.config.*` | Configuration classes (MetadataProperties, CkanProperties, DatabaseConfiguration) |
| `*.core.mapper.*` | Core-level mappers |
| `*.core.errors.*` | Error handling |
| `*.core.security.*` | Security utilities |
| `*.core.ckan.*` | CKAN integration (DTOs, notification, service) |
| `*.core.category.service.*` | Category service |
| `*.core.codelist.service.*` | Codelist service |
| `*.core.concept.service.*` | Concept service |
| `*.core.organisation.service.*` | Organisation service |
| `*.rest.common.dto.*` | REST DTOs (ExternalItemDto, ExternalCkanItemDto, InternationalStringDto) |
| `*.rest.common.mapper.*` | MapStruct mappers for REST layer |
| `*.rest.common.util.*` | PaginationUtil, WebConstants |
| `*.config.*` (internal-web) | KafkaConsumerConfig, KafkaInitializeTopics, KafkaProperties |
| `*.service.*` (internal-web) | KafkaConsumerService, ConsumerProcess implementations |
| `*.ckan.service.*` (internal-web) | CkanService, CkanSenderService implementations |
| `*.health.*` (internal-web) | KafkaHealthIndicator |

## Domain model (JPA)

This project uses standard JPA annotations — **no Sculptor** (.btdesign). Entities are in `edatos-publisher-core/src/main/java/es/gobcan/istac/edatos/publisher/core/domain/`.

### Entities

| Entity | Table | Sequence |
|---|---|---|
| `ExternalItemEntity` | `tb_external_items` | `seq_tb_external_items` (allocationSize=50) |
| `ExternalCkanItemEntity` | `tb_external_ckan_items` | inherits from ExternalItemEntity (JOINED) |
| `InternalEnabledTokenEntity` | `tb_internal_enabled_token` | PK: serviceTicket (no sequence) |
| `AuditEventEntity` | `admin_audit_event` | `seq_admin_audit_event` |
| `CkanPackageRelationshipEntity` | `tb_ckan_package_relationship` | `seq_tb_ckan_package_relationship` |

### Inheritance
- `ExternalItemEntity` (base) → `ExternalCkanItemEntity` (JOINED strategy)

### Value objects
- `InternationalStringVO` — JSONB-mapped multilingual string
- `LocalisedStringVO` — single-language string within InternationalStringVO
- `CkanUrlResource` — CKAN resource definition

### Base classes
- `AbstractVersionedAndAuditingEntity` — combines optimistic locking + audit fields
- `AbstractAuditingEntity` — audit fields (createdDate, createdBy, lastModifiedDate, lastModifiedBy)

### DB sequences
Native PostgreSQL SEQUENCE generators (not custom `TB_SEQUENCES` table).

## Database

### Liquibase migrations
Master changelog: `edatos-publisher-core/src/main/resources/config/liquibase/master.xml`

Changesets in: `edatos-publisher-core/src/main/resources/config/liquibase/changelog/`
- `00000000000000_initial_schema.xml`
- `20210902083432_added_entity_InternalEnabledToken.xml`
- `20220713090732_added_entity_ExternalCkanItemEntity.xml`
- `20230904143532_added_entity_CkanPackageRelationshipEntity.xml`
- `20240415100225_add_column_ckanId_ExternalCkanItemEntity.xml`

### Manual SQL migrations
Scripts for the `common-metadata` schema are in:
```
etc/changes-from-release/{version}/db/common-metadata/postgresql/
```

### Naming strategy
Spring `PhysicalNamingStrategy` + `ImplicitNamingStrategy` — camelCase Java fields map to snake_case columns.

## Kafka integration

The application **consumes** (never produces) Kafka messages from multiple METAMAC/eDatos systems.

### Consumer architecture
- `KafkaConsumerConfig` — Spring `@Configuration` defining consumer factories with Avro deserialization
- `KafkaConsumerService` / `KafkaConsumerServiceImpl` — receives `ConsumerRecord` messages
- `ConsumerProcess` interface — strategy pattern for processing different message types
- `ConsumerProcessFacade` — orchestrates consumer process implementations
- `KafkaInitializeTopics` — ensures topics exist on startup

### Consumer configuration
- Group ID: `edatos-publisher`
- Client ID: `edatos-publisher-client`
- Max poll records: 1 (one message per poll)
- Session timeout: 10s
- Max poll interval: 900000ms (15 min)
- Retry: 3 attempts, 5-second backoff

### Message types and consumer processes

| Avro type | Consumer process |
|---|---|
| `DatasetVersionAvro` | `DatasetPublicationConsumerProcess` |
| `DatasetAvro` | `JaxiDatasetPublicationConsumerProcess` |
| `CodelistAvro` | `CodelistPublicationConsumerProcess` |
| `ConceptSchemeAvro` | `ConceptSchemePublicationConsumerProcess` |
| `OrganisationSchemeAvro` | `OrganisationSchemePublicationConsumerProcess` |
| `CategorySchemeAvro` | `CategorySchemePublicationConsumerProcess` |

### Publication pipeline
1. Kafka consumer receives an Avro record.
2. `ConsumerProcessFacade` routes to the appropriate `ConsumerProcess` implementation.
3. Consumer process transforms the Avro message and calls `CkanService`.
4. `CkanSenderServiceImpl` publishes the resource to CKAN via its REST API.
5. `NotificationService` sends notification on failure.

## CKAN integration

Core purpose of the application — publishes ISTAC statistical resources to a CKAN open-data portal.

### Configuration (MetadataConfigurationService)
| Method | Purpose |
|---|---|
| `retrieveCkanUrlBase()` | CKAN API base URL |
| `retrieveCkanApiKey()` | CKAN API authentication key |
| `retrieveCkanLicenceId()` | Default licence for published datasets |
| `retrieveCkanPublisherUri()` | Publisher URI |
| `retrieveCkanSpatialUri()` | Spatial coverage URI |
| `retrieveCkanEniUrlBase()` | ENI (National Interoperability Schema) URL base |
| `retrieveCkanDefaultUrnCategory()` | Default URN category |
| `retrieveCkanDefaultOrganisation()` | Default CKAN organisation |
| `retrieveCkanDefaultContactEmail()` | Default contact email |
| `retrieveCkanDefaultCreator()` | Default creator |
| `retrieveCkanDefaultTerritoryDescription()` | Default territory description |

### Key services
- `CkanService` / `CkanServiceImpl` — orchestrates CKAN operations
- `CkanSenderServiceImpl` — low-level REST client for CKAN API
- `ExternalCkanItemService` — tracks published items in the local database

## Spring Boot application

### Entry point
`EdatosPublisherInternalApp` in `edatos-publisher-internal-web`:
- `@SpringBootApplication` with `@ComponentScan`, `@EnableAutoConfiguration`, `@EnableConfigurationProperties`

### Configuration files
| File | Purpose |
|---|---|
| `application.yml` | Base config (port 8080, JPA, JHipster, Thymeleaf, i18n) |
| `application-dev.yml` | Development overrides (CORS, debug logging, show-sql) |
| `application-env.yml` | Production/environment config (in `dockerfiles/conf/`) |

### Key properties
```yaml
spring:
  application.name: edatos_publisher
  jpa.database: POSTGRESQL
  jpa.hibernate.ddl-auto: none  # managed by Liquibase
  messages.basename: i18n/messages-publisher
server.port: 8080
```

### Health indicators
- `KafkaHealthIndicator` — custom health check for Kafka connectivity

## Frontend (edatos-publisher-internal-js)

Angular 9 application built with:
- **PrimeNG 9.1.3** — UI components
- **Nebular 5.1.0** — design system
- **ngx-translate** — i18n (HTTP loader)
- **Bootstrap 4.6.0** — styling
- **Quill 1.3.7** — rich text editor
- **Node v12.14.0** — build toolchain

Build output: `webapp/dist/edatos-publisher/` → copied to `target/META-INF/resources/`

## Development rules

### Code language

All code, functions, variables, constants, documentation and comments must be in **English**.
Variable names in camelCase.

### Constants and configuration values

Before defining a constant in code, check whether it already exists in the project configuration services. Search order:

1. **Common library** — `org.siemac.edatos.core.common.conf.ConfigurationService`
   Shared constants across all eDatos projects: default language, internal API URLs, Kafka bootstrap servers, etc.

2. **Project configuration class** — `es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService`
   Publisher-specific constants: CKAN endpoint, API key, default organisation, licence, etc.

#### Strict rule (non-test code)

If a constant is needed for a business or configuration concept (URLs, codelists, cron expressions, roles, etc.) and it is **not found in either class above**:

- **Do not hardcode it directly.**
- Add a standard unresolved-issue marker comment just above (keyword: `FIX` + `ME` concatenated, followed by a colon): `hardcoded constant — verify if it should come from MetadataConfigurationService`.
- Explicitly warn the user before continuing.

### Java version

- **Java 8** — lambdas, streams, method references, `Optional`, and `java.time` are allowed
- This differs from other METAMAC projects (Java 7)

### Technology restrictions

- **No Lombok** — not used in this project; do not introduce it
- **No Sculptor** — this project uses standard JPA entities, not Sculptor-generated code
- **JUnit 4** only (no JUnit 5)
- Use **MapStruct** for object mapping — do not introduce Dozer or manual mapping where MapStruct is appropriate
- Use **Liquibase** for schema changes — do not write raw SQL migration scripts for the application schema (common-metadata schema still uses manual SQL scripts)

### Do / Avoid

**Do:**
- Respect existing library versions — do not update dependencies without consulting
- Follow existing package structure (`es.gobcan.istac.edatos.publisher.*`)
- Use **SLF4J** (`org.slf4j.Logger` / `LoggerFactory`) for logging — supports `{}` placeholder substitution
- Extend existing base entity classes (`AbstractVersionedAndAuditingEntity`, etc.)
- Configure via Spring Boot annotations and `application.yml`
- Use `MetadataConfigurationService` for all CKAN and environment-specific values
- Use MapStruct `@Mapper` interfaces for entity ↔ DTO conversions

**Avoid:**
- Adding Kafka producer code — this application is **consumer-only**
- Modifying Avro schemas — they are defined in `metamac-statistical-resources`, `metamac-statistical-operations`, and `metamac-srm`; consumed here as JAR dependencies
- Using Spring XML configuration — this project uses annotation-based Spring Boot configuration
- Introducing GWT code — the frontend is Angular 9, not GWT

### Logging

This project uses SLF4J with Logback. Use `{}` placeholder substitution (not string concatenation):

```java
private static final Logger LOG = LoggerFactory.getLogger(MyClass.class);

LOG.info("Publishing resource: {}", urn);
LOG.error("Failed to publish {} to CKAN", key, e);
```

### i18n — .properties files

Always maintain all language variants. **Never add a key to one file without adding it to the others.**

**Core module** (`edatos-publisher-core/src/main/resources/i18n/`):

| File | Locale |
|---|---|
| `messages-publisher.properties` | Default |
| `messages-publisher_ca.properties` | Catalan |
| `messages-publisher_en.properties` | English |
| `messages-publisher_es.properties` | Spanish |

**Key prefixes:**
- `constant.publisher.*` — resource type labels (categories, codelists, concepts, datasets, organisations)
- `exception.publisher.*` — error messages
- `notice.*` — CKAN failure notification messages

Always warn the user to review Catalan translations at https://www.softcatala.org/traductor/

## Key conventions

- **Java 8** — lambdas, streams, `Optional`, `java.time` are all allowed
- **Spring Boot annotation-based** configuration — no Spring XML
- **JPA entities** with standard annotations — no Sculptor
- **MapStruct** for object mapping
- **Liquibase** for database schema migrations
- **SLF4J + Logback** for logging with `{}` placeholders
- **Native PostgreSQL sequences** (not `TB_SEQUENCES` table)
- **Kafka consumer-only** — no producer code
- **CKAN REST client** via Apache CXF

## Build

```bash
# Build all modules
mvn clean install

# Build skipping tests
mvn clean install -DskipTests

# Build only the core module
mvn clean install -pl edatos-publisher-core -am

# Build with Maven wrapper
./mvnw clean install
```

### Maven profiles
- **dev** (active by default): includes internal-js frontend build, dev configuration
- **env**: production/environment setup, undertow server

### Frontend build
The Angular frontend is built automatically via `frontend-maven-plugin` during the Maven build of `edatos-publisher-internal-js`. To develop the frontend independently:

```bash
cd edatos-publisher-internal-js/webapp
npm install
npm start   # Angular dev server
```