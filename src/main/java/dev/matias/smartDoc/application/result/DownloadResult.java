package dev.matias.smartDoc.application.result;

import dev.matias.smartDoc.Domain.Document.Document;
import org.springframework.core.io.ByteArrayResource;

public record DownloadResult(Document document, ByteArrayResource resource) {
}
