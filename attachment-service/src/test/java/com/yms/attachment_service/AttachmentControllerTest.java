package com.yms.attachment_service;

import com.yms.attachment_service.controller.AttachmentController;
import com.yms.attachment_service.exceptions.GlobalExceptionHandler;
import com.yms.attachment_service.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AttachmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private AttachmentController attachmentController;

    @BeforeEach
    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();
//
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void uploadFile_shouldReturnCreatedStatus_whenFileIsUploaded() throws Exception {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        String fileName = "testFile.txt";
        when(fileStorageService.saveFile(anyInt(), any(), anyString())).thenReturn(fileName);

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/attachments/{taskId}", 123)
                        .file("file", "test content".getBytes())
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isCreated())
                .andExpect(content().string("File uploaded: " + fileName));
    }

    @Test
    void listFiles_shouldReturnFileList_whenFilesExist() throws Exception {
        // Arrange
        List<String> files = List.of("testFile1.txt", "testFile2.txt");
        when(fileStorageService.listFiles(anyInt(), anyString())).thenReturn(files);

        // Act & Assert
        mockMvc.perform(get("/api/v1/attachments/{taskId}", 123)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("testFile1.txt"))
                .andExpect(jsonPath("$[1]").value("testFile2.txt"));
    }

    @Test
    void deleteFile_shouldReturnSuccessMessage_whenFileIsDeleted() throws Exception {
        // Arrange
        doNothing().when(fileStorageService).deleteFile(anyInt(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(delete("/api/v1/attachments/{taskId}/{fileName}", 123, "testFile.txt")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("File deleted: testFile.txt"));
    }

    @Test
    void downloadFile_shouldReturnFile_whenFileExists() throws Exception {
        // Arrange
        byte[] fileData = "test file content".getBytes();
        when(fileStorageService.getFile(anyInt(), anyString(), anyString())).thenReturn(fileData);

        // Act & Assert
        mockMvc.perform(get("/api/v1/attachments/download/{taskId}/{fileName}", 123, "testFile.txt")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=testFile.txt"))
                .andExpect(content().bytes(fileData));
    }
}
