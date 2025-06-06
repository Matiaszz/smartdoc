package dev.matias.smartDoc.Services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class AzureStorageService {
    private final BlobContainerClient containerClient;

    public AzureStorageService(String connectionString, String containerName){
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString).buildClient();

        containerClient = serviceClient.getBlobContainerClient(containerName);

        if (!containerClient.exists()){
            containerClient.create();
        }

    }

    public void uploadFile(String fileName, byte[] data){
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(new ByteArrayInputStream(data), data.length, true);
    }
}
