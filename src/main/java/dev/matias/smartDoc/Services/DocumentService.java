package dev.matias.smartDoc.Services;


import dev.matias.smartDoc.DTOs.DocumentDTO;
import dev.matias.smartDoc.Domain.DocType;
import dev.matias.smartDoc.Domain.Document;
import dev.matias.smartDoc.Repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.UUID;


@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private AzureStorageService storage;

    @Autowired
    private RepositoriesServices repositoriesServices;

    public void deleteDocument(UUID id){
        Document document = repositoriesServices.getDocumentById(id);
        documentRepository.delete(document);
        storage.deleteFile(document);
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

    public DocumentDTO getDocumentDTO(Document document){
        return new DocumentDTO(document, storage.getMetadata(document));
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
}
