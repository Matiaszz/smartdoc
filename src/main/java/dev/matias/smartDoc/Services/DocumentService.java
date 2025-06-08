package dev.matias.smartDoc.Services;


import dev.matias.smartDoc.Domain.DocType;
import dev.matias.smartDoc.Domain.Document;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
public class DocumentService {
    public DocType getTypeEnum(Document document){
        String name = document.getName();
        String[] imgExtensions = {".jpg", ".jpeg", ".webp", ".png"};

        if (name.endsWith(".pdf")){
            return DocType.PDF;
        }

        if (name.endsWith(".docx")){
            return DocType.DOCX;
        }
        if (name.endsWith(".pptx")){
            return DocType.PPTX;
        }

        if (Arrays.stream(imgExtensions).anyMatch(name::endsWith)) {
            return DocType.IMAGE;
        }

        return DocType.OTHER;
    }
}
