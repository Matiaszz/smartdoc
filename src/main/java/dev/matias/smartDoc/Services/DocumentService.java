package dev.matias.smartDoc.Services;


import dev.matias.smartDoc.Domain.DocType;
import dev.matias.smartDoc.Domain.Document;
import dev.matias.smartDoc.Repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;


@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;


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
