package dev.matias.smartDoc.Controllers;

import dev.matias.smartDoc.DTOs.DocumentDTO;
import dev.matias.smartDoc.Domain.Document;
import dev.matias.smartDoc.Services.AzureStorageService;
import dev.matias.smartDoc.Services.DocumentService;
import dev.matias.smartDoc.Services.RepositoriesServices;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@AllArgsConstructor
public class DocumentController {

    private final RepositoriesServices repositoriesServices;
    private final AzureStorageService storageService;
    private final DocumentService documentService;


    @PostMapping("/upload/")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {

            Document savedDocument = documentService.prepareDocument(file);
            documentService.validateDocument(savedDocument);
            storageService.uploadFile(savedDocument, file.getBytes());

            return ResponseEntity.ok().body(
                    new DocumentDTO(savedDocument, storageService.getMetadata(savedDocument)
                    )
            );

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on file processing: %s", e);
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable UUID id){
        Document document = repositoriesServices.getDocumentById(id);
        return ResponseEntity.ok().body(
                new DocumentDTO(document, storageService.getMetadata(document))
        );
    }

    @GetMapping("/download/{id}/")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable UUID id) {
        Document document = repositoriesServices.getDocumentById(id);

        ByteArrayResource resource = storageService.downloadFile(document);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + document.getName() + "\"")
                .body(resource);
    }

    @GetMapping("/all/")
    public List<DocumentDTO> getAllDocuments(){
        return storageService.getAllFiles();
    }

}
