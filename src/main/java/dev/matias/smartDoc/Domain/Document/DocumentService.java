package dev.matias.smartDoc.Domain.Document;


import dev.matias.smartDoc.Domain.Document.ValueObjects.DocType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.UUID;


@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public Document deleteDocument(UUID id){
        Document document = this.getDocumentById(id);
        documentRepository.delete(document);
        return document;

    }


    public DocType getTypeEnum(String fileName){
        String[] imgExtensions = {".jpg", ".jpeg", ".webp", ".png"};

        if (fileName.endsWith(".pdf")){
            return DocType.PDF;
        }

        if (fileName.endsWith(".docx")){
            return DocType.DOCX;
        }
        if (fileName.endsWith(".pptx")){
            return DocType.PPTX;
        }

        if (Arrays.stream(imgExtensions).anyMatch(fileName::endsWith)) {
            return DocType.IMAGE;
        }

        return DocType.OTHER;
    }

    public Document getDocumentById(UUID documentId){
        return documentRepository.findById(documentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document ID not found."));
    }

    public Document prepareDocument(MultipartFile file){
        Document doc = new Document();
        doc.setName(file.getOriginalFilename());
        doc.setType(getTypeEnum(doc.getName()));
        doc = documentRepository.save(doc);
        doc.setBlobName();
        doc = documentRepository.save(doc);
        documentRepository.flush();
        return doc;
    }

    public void validateDocument(Document document){
        if (document.getType() == DocType.OTHER){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type.");
        }

        if (document.getName().trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document name is required.");
        }
    }
}
