package dev.matias.smartDoc.Config;

import dev.matias.smartDoc.Services.AzureStorageService;
import dev.matias.smartDoc.Services.DocumentService;
import dev.matias.smartDoc.Services.RepositoriesServices;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public DocumentService documentService() {
        return Mockito.mock(DocumentService.class);
    }
    @Bean
    public AzureStorageService azureStorageService() {
        return Mockito.mock(AzureStorageService.class);
    }

    @Bean
    public RepositoriesServices repositoriesServices() {
        return Mockito.mock(RepositoriesServices.class);
    }
}
