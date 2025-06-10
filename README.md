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

# 🔜 Next Steps (Sprint 2 Preview)
- Document deletion

- User authentication (possibly via Spring Security)

- Azure Blob metadata updates

- Frontend integration (React)

