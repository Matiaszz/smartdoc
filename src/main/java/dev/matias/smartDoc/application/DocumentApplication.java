package dev.matias.smartDoc.application;

import dev.matias.smartDoc.Domain.Document.Document;
import dev.matias.smartDoc.Domain.Document.DocumentService;
import dev.matias.smartDoc.Infra.storage.AzureStorageService;
import dev.matias.smartDoc.Interfaces.dto.DocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class DocumentApplication {
    @Autowired
    private DocumentService documentService;

    @Autowired
    private AzureStorageService storageService;

    public DocumentDTO uploadFile(MultipartFile file){


        try {
            Document savedDocument = documentService.prepareDocument(file);
            documentService.validateDocument(savedDocument);
            storageService.upload(savedDocument, file.getBytes());

            return new DocumentDTO(
                    savedDocument,
                    storageService.getMetadata(savedDocument)
            );


        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on file processing: %s", e);
        }
    }
}
