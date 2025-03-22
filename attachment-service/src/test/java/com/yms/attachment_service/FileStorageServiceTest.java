package com.yms.attachment_service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import com.yms.attachment_service.service.FileStorageService;
import com.yms.attachment_service.service.abstracts.TaskClientService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

class FileStorageServiceTest {

    @Mock
    private TaskClientService taskClientService;

    @InjectMocks
    private FileStorageService fileStorageService;

    private static final String FILE_NAME = "testFile.txt";


    @BeforeEach
    void setUp() {
        try (AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
            System.out.println("Worked!");
        } catch (Exception e) {
            fail("Failed to initialize mocks: " + e.getMessage());
        }
    }


    @Test
    void saveFile_shouldThrowRuntimeException_whenFileCannotBeSaved() throws Exception {

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(FILE_NAME);
        doThrow(new IOException("Unable to write file")).when(file).transferTo(any(File.class));
        doNothing().when(taskClientService).findTaskById(anyInt(), anyString());


        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            fileStorageService.saveFile(123, file, "token")
        );

        assertEquals("File could not be saved! Error: Unable to write file", exception.getMessage());
    }

}


