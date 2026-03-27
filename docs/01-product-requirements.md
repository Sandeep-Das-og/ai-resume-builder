# AI Resume Builder - Product Requirements (MVP)

## Goals
- Help job seekers and recruiters create ATS-safe resumes quickly.
- Provide templates, upload analysis, ATS scoring, and AI suggestions.
- No persistent storage of uploaded resumes.

## Target Users
- Job seekers in India and the US.
- Recruiters who want to polish candidate resumes.

## Scope (MVP)
1. Resume builder with pre-defined ATS-safe templates (included in app).
2. Resume upload (PDF, DOCX, TXT) with ATS scoring (0-100).
3. AI-powered improvement suggestions for uploaded resumes.
4. Export resumes to PDF and DOCX.
5. Email resumes created in the builder.

## Non-Goals (MVP)
- User accounts or saved projects.
- Payments or paid plans.
- Multilingual support.

## UX Principles
- Modern, production-grade visual style.
- Clear, guided flow for creating or improving a resume.
- Minimal friction and fast feedback.

## Privacy & Data Policy (MVP)
- No persistent storage of uploaded resumes.
- Temporary in-memory processing only.
- Generated resumes exist in the browser and in-memory on server only for export/email.

## Assumptions (Confirm)
- Users may optionally paste a job description for ATS scoring.
- If no job description is provided, use a general ATS scoring rubric.
