# Changelog

## 0.1.5 - 2026-03-31
- Added per-IP rate limiting filter and configuration.
- Added validation guard utility and applied to email/export/ATS file validation.
- Added unit/integration tests for rate limiting and validation guard.
- Updated backlog to remove completed validation/rate limiting item.

## 0.1.4 - 2026-03-29
- Added Brevo email send endpoint with validation and service layer.
- Added Brevo HTTP client integration and payload models.
- Added unit/integration tests for email sending workflow.
- Updated backlog to remove completed email item.

## 0.1.3 - 2026-03-29
- Added export endpoints for PDF and DOCX generation.
- Added export service, request model, and configuration.
- Added unit/integration tests for export pipeline.
- Added PDFBox and Apache POI dependencies.
- Updated backlog to remove completed export item and stale completed items.

## 0.1.2 - 2026-03-27
- Added ATS suggestions endpoint (rules-based) with unit and integration tests.
- Added suggestion result model and service layer.
- Updated backlog to remove completed ATS suggestions item.

## 0.1.1 - 2026-03-27
- Initialized mono-repo structure (Angular frontend + Spring Boot backend).
- Added ATS-safe resume templates (Modern Minimal, Professional Classic, Technical Focus, Executive).
- Implemented ATS scoring backend (upload, parsing, validation, scoring breakdown).
- Added unit/integration test segregation and Gradle tasks.
- Added root Gradle build to orchestrate frontend + backend builds.
- Added CI workflow for full repo build.
- Added product/docs backlog and initial documentation.
