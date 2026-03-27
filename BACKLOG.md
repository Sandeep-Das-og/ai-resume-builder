# Backlog

## Backend
- Implement resume upload endpoint (PDF/DOCX/TXT) with in-memory parsing.
- Build ATS scoring engine (rules + AI) and expose scoring API.
- Add AI suggestion endpoint with optional job description input.
- Implement resume export endpoints (PDF and DOCX).
- Add email send endpoint (Brevo integration, env-driven).
- Add file validation (size, type) and basic rate limiting.

## Frontend
- Build template selection and editor UI.
- Implement upload flow with score + suggestions view.
- Add resume preview and export actions.
- Add job description input UI (optional).

## Templates
- Generate preview images for each template.
- Map template placeholders to real form fields.

## DevOps
- Add Dockerfiles for frontend and backend.
- Add Render deployment config (service + env vars).
- Add basic healthcheck endpoint.

## QA
- Add unit tests for ATS scoring rubric.
- Add parsing tests for PDF/DOCX/TXT.
