# AI Resume Builder

Mono-repo for the AI Resume Builder (Angular + Spring Boot).

## Structure
- `frontend/` Angular app
- `backend/` Spring Boot (Java)
- `docs/` product + design docs

## Templates
ATS-safe templates are in:
- `backend/src/main/resources/templates`

## Run Locally (Manual)
Frontend:
- `cd frontend`
- `npm install`
- `npm run start`

Backend:
- `cd backend`
- `gradle bootRun`

## Notes
- Uploaded resumes are not persisted.
- In-memory parsing and scoring only.
