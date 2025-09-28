package dev.matias.smartDoc.Interfaces;

import dev.matias.smartDoc.Interfaces.dto.document.DocumentDTO;
import dev.matias.smartDoc.application.DocumentApplication;
import dev.matias.smartDoc.application.result.DownloadResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@AllArgsConstructor
public class DocumentController {

    @Autowired
    private DocumentApplication documentApplication;

    @Operation(
            summary = "Upload a file to azure Storage",
            description = "Accepts any file with a max size of 10MB and send to cloud.")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

        DocumentDTO dto = documentApplication.uploadFile(file);
        return ResponseEntity.ok(dto);
    }


    @Operation(
            summary = "Delete a file from azure and DB",
            description = "You must provide the document ID to proceed with the file deletion")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        documentApplication.deleteDocument(id);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Get a document by ID",
            description = "You must provide the document ID to get the document")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable UUID id){
        DocumentDTO dto = documentApplication.getDocumentById(id);
        return ResponseEntity.ok(dto);
    }


    @Operation(
            summary = "Make the download of the specified file",
            description = "You must provide the document ID to do the download of the specified document")
    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable UUID id) {
        DownloadResult result = documentApplication.downloadFile(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + result.document().getName() + "\"")
                .body(result.resource());
    }


    @GetMapping("/all")
    public List<DocumentDTO> getAllDocuments(){
        return documentApplication.getAllDocuments();
    }
}
