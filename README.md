# SmartDoc - Document Management API
## 📑 Overview
SmartDoc is a document management backend system built with Spring Boot and integrated with Azure Blob Storage.
It enables users to upload, store, list, and download documents securely from the cloud.

## 🚀 Features (Sprint 1)
- ✅ Upload PDF files to Azure Blob Storage
- ✅ Download PDF files by ID
- ✅ List all uploaded documents with metadata
- ✅ Store document data in MySQL database (via JPA repository)


## 🧩 API Endpoints
### Upload a file
- POST /api/documents/upload/

  - Form Data: 
  - file: MultipartFile

  - Response: DocumentDTO (document info + Azure metadata)

### Download a file
- GET /api/documents/download/{id}/

  - Path Variable: id: UUID

  - Response: File download (application/octet-stream)

### Get all documents
- GET /api/documents/all/

    - Response: List of DocumentDTO


SETUPS
----------------------------
- Remove "-example" from the following files: *docker-compose* and *application* files;
- Overwrite with your personal configuration (such as Azure keys, DB credentials, etc);
- Ensure Docker is running;
- Required Tools:
  - Java 17+

  -  Docker & Docker Compose
    
  -  Azure Storage Account
    
  -  MySQL (configured via Docker)

## 📌 Sprint 1 Summary
- Basic CRUD for documents implemented (Upload, Download, List)

- Azure Blob Storage integrated

- MySQL connected via Spring Data JPA

- DTO and metadata handling created

- Error handling added via ResponseStatusException

# 🎯 Sprint 2 Goals (AI-enhanced version)
## 🎨 1. Develop FastAPI Service for Automated Document Generation (Python)
- Create a REST API using FastAPI to handle document automation tasks.

- Receive JSON POST request containing a prompt/text request.

- Use AI model (OpenAI GPT-4o or local model) to generate text based on the prompt.

- Generate a PDF file from the generated text using WeasyPrint or FPDF.

- Save the PDF file temporarily on the disk for further processing.

## 🔗 2. Integrate FastAPI with Spring Boot Backend
- Send the generated PDF file from FastAPI to Spring Boot's /api/documents/upload/ endpoint using an HTTP POST request (multipart/form-data).

- Test local communication (localhost:8080).

- Validate if the uploaded document reaches Azure Blob Storage successfully (visible via /all/).

- ☁️ 3. Spring Boot Backend Improvements (if needed)
- Ensure /upload/ endpoint accepts requests sent from FastAPI.

- Handle "AI-generated" uploads properly:

- Fixed or auto-generated metadata (e.g., source = FastAPI-AI).

- Verify documents received via FastAPI appear correctly in the /all/ endpoint list.

## ✅ 4. Testing & Validation
- Test complete flow: FastAPI → Spring Boot → Azure Blob Storage → GET via Frontend/Postman.

- Confirm document availability and proper download via /download/{id}/.