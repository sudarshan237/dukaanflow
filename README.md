# DukaanFlow

**Target statement: Automation B1 — Replace a department, not a task.**

DukaanFlow is an AI-assisted enquiry-to-order operations workspace for small local retailers. It aims to automate the sales-enquiry function from customer message intake and intent triage through product matching, quote preparation, follow-up and order confirmation, while keeping the seller in control of customer-facing commitments.

## Current implementation status

- Interactive seller dashboard prototype: index.html
- Spring Boot REST API foundation
- Enquiry intake with input validation
- Deterministic baseline for intent, product and quantity extraction
- Human-review state and seller-controlled status updates
- Not yet implemented: PostgreSQL persistence, live LLM integration, catalogue/stock validation, scheduled follow-ups, audit trail, evaluation dataset and deployment

Important: the current classifier is a deterministic baseline, not a live AI integration. The existing dashboard contains sample data and simulated interactions. Do not present simulated behaviour as production AI.

## Run the API

Requirements: Java 21 and Maven 3.9+.

    mvn spring-boot:run

API base URL: http://localhost:8080

Endpoints:
- GET /actuator/health
- GET /api/enquiries
- POST /api/enquiries
- PATCH /api/enquiries/{id}/status

Example request body:
    {"customerName":"Aarav","message":"Do you have 2 coffee mugs available?"}

## Intended workflow

1. Intake: accept a customer message.
2. Triage: extract intent, product, quantity and missing information into a validated schema.
3. Grounding: resolve product facts only from the seller's catalogue and stock.
4. Decision: route uncertain or policy-sensitive cases to human review.
5. Execution: prepare a reply or quote; do not send or confirm without seller approval.
6. Follow-up: track deadlines, retries and unresolved enquiries.
7. Evidence: evaluate extraction accuracy, groundedness, handling time and exception rates.

## Safety and data

Use synthetic customer data only. Never commit API keys. List external models, APIs and open-source dependencies here before submission.

## Hackathon

Homingo Hackathon · Edition 01 · AI for real local commerce. Build phase: 21 Nov–6 Dec 2026. Submission window: 4–6 Dec 2026. The final track selected in the Homingo dashboard must match the project.
