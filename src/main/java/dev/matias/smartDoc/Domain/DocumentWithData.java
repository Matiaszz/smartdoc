package dev.matias.smartDoc.Domain;

import lombok.Data;

import java.util.UUID;

@Data
public class DocumentWithData{

    private UUID id;

    private DocType type;
    private String blobName;
    private String name;
    private long size;

    private byte[] data;

    public DocumentWithData(Document document, byte[] data){
        this.id = document.getId();
        this.type = document.getType();
        this.blobName = document.getBlobName();
        this.name = document.getName();
        this.size = data != null ? data.length : 0;
        this.data = data;
    }

}
