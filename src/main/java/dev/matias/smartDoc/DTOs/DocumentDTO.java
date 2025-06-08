package dev.matias.smartDoc.DTOs;

import dev.matias.smartDoc.Domain.DocType;
import dev.matias.smartDoc.Domain.Document;

import java.util.UUID;

public record DocumentDTO (UUID id, String name, DocType type) {
    public DocumentDTO(Document document){
        this(document.getId(), document.getName(), document.getType());
    }
}
