package dev.matias.smartDoc.Services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import dev.matias.smartDoc.Domain.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class AzureStorageService {
    private final BlobContainerClient containerClient;

    public AzureStorageService(@Value("${azure.storage.connection-string}") String connectionString,
                               @Value("${azure.storage.container-name}") String containerName){
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString).buildClient();

        containerClient = serviceClient.getBlobContainerClient(containerName);

        if (!containerClient.exists()){
            containerClient.create();
        }

    }

    public void uploadFile(Document document){
        BlobClient blobClient = containerClient.getBlobClient(document.getName());
        blobClient.upload(new ByteArrayInputStream(document.getData()), document.getData().length, true);
    }
}
