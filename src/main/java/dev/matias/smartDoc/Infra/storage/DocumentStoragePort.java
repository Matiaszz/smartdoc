package dev.matias.smartDoc.Infra.storage;

import dev.matias.smartDoc.Domain.Document.Document;
import org.springframework.core.io.ByteArrayResource;

import java.util.Map;

public interface DocumentStoragePort {
    void upload(Document document, byte[] data);
    void delete(Document document);
    ByteArrayResource download(Document document);
    Map<String, String> getMetadata(Document document);
}
