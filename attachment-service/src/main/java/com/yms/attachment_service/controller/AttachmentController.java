package com.yms.attachment_service.controller;

import com.yms.attachment_service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final FileStorageService fileStorageService;

    @PostMapping("/{taskId}")
    public ResponseEntity<String> uploadFile(
            @PathVariable Integer taskId,
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token) {

        String fileName = fileStorageService.saveFile(taskId, file,token);
        return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded: " + fileName);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<List<String>> listFiles(@PathVariable Integer taskId,@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(fileStorageService.listFiles(taskId,token));
    }

    @DeleteMapping("/{taskId}/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable Integer taskId, @PathVariable String fileName,@RequestHeader("Authorization") String token) {
        fileStorageService.deleteFile(taskId, fileName,token);
        return ResponseEntity.ok("File deleted: " + fileName);

    }

    @GetMapping("/download/{taskId}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer taskId, @PathVariable String fileName, @RequestHeader("Authorization") String token) {
        byte[] fileData = fileStorageService.getFile(taskId, fileName,token);
        ByteArrayResource resource = new ByteArrayResource(fileData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}

