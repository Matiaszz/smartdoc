package dev.matias.smartDoc;

import dev.matias.smartDoc.Config.TestConfig;
import dev.matias.smartDoc.Domain.Document;
import dev.matias.smartDoc.Services.AzureStorageService;
import dev.matias.smartDoc.Services.DocumentService;
import dev.matias.smartDoc.Services.RepositoriesServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Import(TestConfig.class)
@AutoConfigureMockMvc
class SmartDocApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private AzureStorageService storageService;

	@Autowired
	private RepositoriesServices repositoriesServices;

	private final UUID mockId = UUID.randomUUID();

	private final String uri = "/api/documents/" + mockId + "/";

	private final Document mockDocument = Document.builder()
			.id(mockId)
			.name("test.pdf")
			.build();

	@Test
	void shouldGetAllDocuments() throws Exception {
		mockMvc.perform(get("/api/documents/all/"))
				.andExpect(status().isOk());
	}

	@Test
	void shouldUploadFileSuccessfully() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
		when(documentService.prepareDocument(any())).thenReturn(mockDocument);
		doNothing().when(documentService).validateDocument(any());
		doNothing().when(storageService).uploadFile(any(), any());

		mockMvc.perform(multipart("/api/documents/upload/").file(file))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("test.pdf"));
	}

	@Test
	void shouldGetADocument() throws Exception {
		when(repositoriesServices.getDocumentById(any())).thenReturn(mockDocument);
		mockMvc.perform(get(uri)).andExpect(status().isOk());
	}

	@Test
	void shouldDownloadAFile() throws Exception {
		when(repositoriesServices.getDocumentById(any())).thenReturn(mockDocument);
		when(storageService.downloadFile(any())).thenReturn(new ByteArrayResource("dummy content".getBytes()));

		mockMvc.perform(get("/api/documents/download/" + mockId + "/"))
				.andExpect(status().isOk());
	}

	@Test
	void shouldDeleteAFile() throws Exception {
		doNothing().when(documentService).deleteDocument(any());
		mockMvc.perform(delete(uri)).andExpect(status().isOk());
	}
}