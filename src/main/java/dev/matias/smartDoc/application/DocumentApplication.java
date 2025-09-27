package dev.matias.smartDoc.application;

import dev.matias.smartDoc.Domain.Document.Document;
import dev.matias.smartDoc.Domain.Document.DocumentService;
import dev.matias.smartDoc.Infra.storage.AzureStorageService;
import dev.matias.smartDoc.Interfaces.dto.DocumentDTO;
import dev.matias.smartDoc.application.result.DownloadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    public void deleteDocument(UUID id){
        Document documentToBeDeleted = documentService.deleteDocument(id);
        storageService.delete(documentToBeDeleted);
    }

    public DocumentDTO getDocumentById(UUID id){
        Document document = documentService.getDocumentById(id);
        return new DocumentDTO(document, storageService.getMetadata(document));
    }

    public DownloadResult downloadFile(UUID id){
        Document document = documentService.getDocumentById(id);

       return new DownloadResult(document, storageService.download(document));
    }

    public List<DocumentDTO> getAllDocuments(){
        return storageService.getAllFiles();
    }
}
