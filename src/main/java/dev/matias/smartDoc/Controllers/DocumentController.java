package dev.matias.smartDoc.Controllers;

import dev.matias.smartDoc.DTOs.DocumentDTO;
import dev.matias.smartDoc.Domain.Document;
import dev.matias.smartDoc.Repositories.DocumentRepository;
import dev.matias.smartDoc.Services.AzureStorageService;
import dev.matias.smartDoc.Services.DocumentService;
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
    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            Document document = new Document();
            document.setName(file.getOriginalFilename());
            document.setType(documentService.getTypeEnum(document));
            document.setData(file.getBytes());

            documentRepository.save(document);

            storageService.uploadFile(document);

            return ResponseEntity.ok(new DocumentDTO(document));

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error in file process: " + e.getMessage());
        }
    }
}
