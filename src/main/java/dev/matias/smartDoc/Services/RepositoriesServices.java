package dev.matias.smartDoc.Services;

import dev.matias.smartDoc.Domain.Document;
import dev.matias.smartDoc.Repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class RepositoriesServices {

    @Autowired
    private DocumentRepository documentRepository;

    public Document getDocumentById(UUID documentId){
        return documentRepository.findById(documentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document ID not found."));
    }

}
