# S3 JSON → Database Ingest (AWS Lambda, Java, AWS SAM, SQS Required)

This AWS Lambda service ingests review JSON files from **Amazon S3** via **Amazon SQS** and stores the data in a **relational database**.
It is optimized for small files (KB range) and built for reliability and maintainability.

---

## Features

* **SQS required**: All S3 events must go through SQS before Lambda processing.
* Parses JSON and validates required fields.
* Saves data into the database in a **single transaction**.
* Modular Java 21 code using JPA/Hibernate.
* Fully deployable via **AWS SAM CLI**.

---

## Architecture

```
S3 Object (JSON)
   │
   └─▶ S3 Event Notification
            │
            └─▶ SQS Queue
                    │
                    └─▶ Lambda Handler ─▶ Parser ─▶ Validator ─▶ Persistence (JPA/Hibernate)
                                                                   └─▶ DB (PostgreSQL, etc.)
```

**Why SQS is required**

* Decouples S3 from Lambda for better failure handling.
* Allows retries, batching, and DLQ support.
* Prevents event loss during downstream failures.

---

## Project Structure

* **ReviewIngestFromSqsHandler** – Lambda entrypoint for SQS events.
* **IngestWorker** – Orchestrates S3 download, parse, validate, save.
* **ReviewService** – Core business flow.
* **ReviewDataParser** – JSON → domain object conversion.
* **ReviewPersistenceService** – Domain → JPA entity mapping.
* **ReviewRepository** – JPA operations & transactions.
* **Entities** – `HotelEntity`, `ReviewEntity`, `ReviewerEntity`, etc.
* **OrmConfig** – DataSource, HikariCP, Hibernate setup.

---

## Requirements

* Java 21
* AWS SAM CLI
* Maven 3.9+
* AWS account with:

  * Lambda
  * S3
  * SQS
  * IAM role with required permissions
* PostgreSQL  reachable from Lambda

---

## Database Configuration

Please fill Database details in template.yaml with actual value

-DB_URL: jdbc:postgresql://XXXX

-DB_USER: AAAAAAAA

-DB_PASSWORD: YYYYYYYY

## Build & Deploy

**Build:**

```bash
sam build
```

**Deploy (guided):**

```bash
sam deploy --guided
```

You’ll be prompted for all parameters. AWS SAM will remember your answers for the next deploy.



## Example Input JSON

### Can we found in Samples folder

# Quick Deployment and Testing
- Configure DB Details in Template.yaml file
- Make Build do deployment
- aws s3 cp sample1-agoda.json s3://review-dev-30-ingestbucket-tehrhptcoscu/
- aws logs tail /aws/lambda/review-dev-30-ReviewIngestFromSqs-kqrvU1sqd7rE --region ap-southeast-1 --since 5m --follow
- aws logs tail /aws/lambda/review-dev-30-ReviewApi-BpAdJAgz3noD --region ap-southeast-1 --since 5m --follow
- Review comment API can be called to get the deatils of review by passing hotelId (./reviews?hotelId=555)
- **Please note i have Put DB deatils but values has been fabircated so it will not work, please put your actual value**



---

## Error Handling

* **Parsing/validation errors**: Logged with details; failed messages stay in SQS until handled or moved to DLQ.
* **Transient DB/S3 errors**: Exceptions bubble up; message retried per SQS redrive policy.
* **SQS DLQ**: Mandatory for production to capture poison messages.

---



