package dev.matias.smartDoc.DTOs;

import dev.matias.smartDoc.Domain.DocType;
import dev.matias.smartDoc.Domain.Document;

import java.util.Map;
import java.util.UUID;

public record DocumentDTO (UUID id, String name, String blobName, DocType type, Map<String, String> metadata) {
    public DocumentDTO(Document document, Map<String, String> metadata){
        this(document.getId(), document.getName(), document.getBlobName(), document.getType(), metadata);
    }

}
