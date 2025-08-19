package com.devcambodia.HPK_Booking.controller;

import com.devcambodia.HPK_Booking.dto.res.FileResponse;
import com.devcambodia.HPK_Booking.service.FileService;
import com.devcambodia.HPK_Booking.utils.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final FileService fileService;
    private final CustomResponse customResponse;
    @Value("${client_address}")
    private String fileStorage;


    @PostMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<Object> uploadSingleFile(
            @RequestPart("file") MultipartFile file, HttpServletRequest request) {
            FileResponse fileResponse = fileService.uploadSingleFile(file, request);
            log.info("File uploaded successfully");
        return customResponse.success("Upload File successfully",fileResponse, HttpStatus.CREATED);
    }
    @GetMapping("/images/{filename}")
    public ResponseEntity<UrlResource> getImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get(fileStorage + filename);
        UrlResource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            log.info("Get image successfully");
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            log.info("Get image failed");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/multiple", consumes = "multipart/form-data")
    public ResponseEntity<Object> uploadMultipleFiles(
            @RequestPart("files") MultipartFile[] files, HttpServletRequest request) {
        log.info("File uploaded successfully");
        return ResponseEntity.ok(fileService.uploadMultipleFiles(files, request));
    }
}
