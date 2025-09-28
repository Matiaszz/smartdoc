package dev.matias.smartDoc.Infra.storage;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.ListBlobsOptions;
import dev.matias.smartDoc.Interfaces.dto.document.DocumentDTO;
import dev.matias.smartDoc.Domain.Document.Document;
import dev.matias.smartDoc.Domain.Document.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AzureStorageService implements DocumentStoragePort{
    private final BlobContainerClient containerClient;

    @Autowired
    private DocumentService documentService;

    public AzureStorageService(@Value("${azure.storage.connection-string}") String connectionString,
                               @Value("${azure.storage.container-name}") String containerName){
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString).buildClient();

        containerClient = serviceClient.getBlobContainerClient(containerName);

        if (!containerClient.exists()){
            containerClient.create();
        }

    }

    @Override
    public void upload(Document document, byte[] data){
        BlobClient blobClient = getBlobClient(document);
        blobClient.upload(new ByteArrayInputStream(data), data.length, true);

        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("documentId", String.valueOf(document.getId()));
        // metadata.put("userId", "userId in the future");
        blobClient.setMetadata(metadata);
    }

    @Override
    public void delete(Document document){
        BlobClient blobClient = getBlobClient(document);
        blobClient.deleteIfExists();
    }

    public List<DocumentDTO> getAllFiles() {
        PagedIterable<BlobItem> blobs = containerClient.listBlobs(
                new ListBlobsOptions().setDetails(new BlobListDetails().setRetrieveMetadata(true)),
                null);

        return blobs.stream()
                .map(this::convertBlobItemToDocument)
                .map(doc -> new DocumentDTO(doc, getMetadata(doc)))
                .collect(Collectors.toList());
    }

    @Override
    public ByteArrayResource download(Document document){
        BlobClient file = containerClient.getBlobClient(document.getBlobName());

        byte[] fileContent = file.downloadContent().toBytes();

        return new ByteArrayResource(fileContent);
    }

    @Override
    public Map<String, String> getMetadata(Document document) {
        return containerClient.getBlobClient(document.getBlobName()).getProperties().getMetadata();
    }

    private Document convertBlobItemToDocument(BlobItem item) {
        String documentIdStr = item.getMetadata() != null ? item.getMetadata().get("documentId") : null;
        UUID documentId = (documentIdStr != null) ? UUID.fromString(documentIdStr) : null;

        if (documentIdStr == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID not casted");
        }

        return documentService.getDocumentById(documentId);
    }

    private BlobClient getBlobClient(Document document){
        String uniqueBlobName = document.getBlobName();
        return containerClient.getBlobClient(uniqueBlobName);
    }


}
