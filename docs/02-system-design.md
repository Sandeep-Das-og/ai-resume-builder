# AI Resume Builder - System Design (MVP)

## Stack
- Frontend: Angular (SPA)
- Backend: Java + Spring Boot (REST API)
- Document parsing: Apache Tika (PDF/DOCX/TXT)
- Resume rendering: HTML to PDF (server) + DOCX export
- Containerization: Docker
- Deployment target: Render (free tier)

Note: Render supports Docker-based services. Kubernetes is not required for MVP. Keep the app Docker-only for now, and add K8s manifests later if needed.

## High-Level Architecture
- Client (Angular)
  - Template selection and editing
  - Upload and analysis UX
  - Export and email actions
- API (Spring Boot)
  - Template catalog
  - Resume parsing
  - ATS scoring
  - AI suggestions
  - Export endpoints
  - Email sending

## Core Flows
### 1) Build Resume from Template
- Client loads templates from API.
- User edits resume in browser.
- Client renders preview.
- Export: server generates PDF/DOCX for consistent formatting.

### 2) Upload Resume for Scoring
- Client uploads file (PDF/DOCX/TXT).
- Server parses content (in-memory only).
- Server computes ATS score and insights.
- Client displays score and AI suggestions.

### 3) Email Resume
- Client sends a request with rendered content.
- Server generates PDF/DOCX in-memory and emails it.
- No server-side persistence of files.

## Data Handling (No Storage)
- Uploads are streamed to memory or temp buffers and deleted immediately.
- No DB required for MVP.
- Template metadata stored as static JSON + HTML/CSS templates in backend resources.

## Security
- Max file size limits and content type validation.
- Virus/malware scanning can be added later.
- Rate limiting per IP to prevent abuse.

## Observability
- Basic request logging and error metrics.
- Structured logs for scoring pipeline.

## Deployment (Render)
- Single Docker service for API and static SPA hosting.
- Enable gzip compression and caching for templates.
