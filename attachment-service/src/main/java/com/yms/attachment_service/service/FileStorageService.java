package com.yms.attachment_service.service;

import com.yms.attachment_service.exceptions.FileNotFoundException;
import com.yms.attachment_service.exceptions.exception_response.ErrorMessages;
import com.yms.attachment_service.service.abstracts.TaskClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final TaskClientService taskService;
    private static final String UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads").toString();

    private Path getTaskDir(Integer taskId) {
        return Paths.get(UPLOAD_DIR, taskId.toString());
    }

    public String saveFile(Integer taskId, MultipartFile file,String token) {

        taskService.findTaskById(taskId,token);

        try {
            Path taskPath = getTaskDir(taskId);
            if (!Files.exists(taskPath)) {
                Files.createDirectories(taskPath);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = taskPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(String.format(ErrorMessages.FILE_COULD_NOT_SAVE,e.getMessage()));
        }
    }

    public List<String> listFiles(Integer taskId,String token) {

        taskService.findTaskById(taskId,token);

        Path taskPath = getTaskDir(taskId);
        if (!Files.exists(taskPath)) {
            return List.of();
        }

        File[] files = taskPath.toFile().listFiles();
        return files != null ? Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList()) : List.of();
    }

    public void deleteFile(Integer taskId, String fileName, String token) {

        taskService.findTaskById(taskId,token);

        Path filePath = getTaskDir(taskId).resolve(fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(String.format(ErrorMessages.FILE_COULD_NOT_DELETE,e.getMessage()));
        }
    }

    public byte[] getFile(Integer taskId, String fileName, String token) {
        taskService.findTaskById(taskId, token);

        Path filePath = getTaskDir(taskId).resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(String.format(ErrorMessages.FILE_NOT_FOUND,fileName,taskId));
        }

        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(String.format(ErrorMessages.FILE_COULD_NOT_READ,e.getMessage()));
        }
    }
}

