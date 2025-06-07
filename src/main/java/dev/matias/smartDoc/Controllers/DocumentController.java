package dev.matias.smartDoc.Controllers;

import dev.matias.smartDoc.Domain.Document;
import dev.matias.smartDoc.Domain.DocType;
import dev.matias.smartDoc.Repositories.DocumentRepository;
import dev.matias.smartDoc.Services.AzureStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
@AllArgsConstructor
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final AzureStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") DocType type
    ) {
        try {
            Document document = new Document();
            document.setName(file.getOriginalFilename());
            document.setType(type);
            document.setData(file.getBytes());

            documentRepository.save(document);

            storageService.uploadFile(document);

            return ResponseEntity.ok("Uploaded, ID: " + document.getId());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error in file process: " + e.getMessage());
        }
    }
}
