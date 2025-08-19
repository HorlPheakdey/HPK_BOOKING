package com.devcambodia.HPK_Booking.service.impl;

import com.devcambodia.HPK_Booking.dto.res.FileResponse;
import com.devcambodia.HPK_Booking.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Value("${server_address}")
    private String fileStorage;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpeg","jpg","png","gif",
            "image/jpeg", "image/jpg", "image/png", "image/gif"
    );
    private String generateUrl(String fileName, HttpServletRequest request) {
        return String.format("%s://%s:%d/api/v1/file/images/%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                fileName);
    }
    private String generateDownloadUrl(String fileName,HttpServletRequest request) {
        return String.format("%s://%s:%d/api/v1/file/download/%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                fileName);
    }
    private String uploadFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (!ALLOWED_EXTENSIONS.contains(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    contentType + " not allowed!!");
        }
        try {
            Path fileStoragePath = Path.of(fileStorage);
            if (!Files.exists(fileStoragePath)) {
                try {
                    Files.createDirectories(fileStoragePath);
                } catch (IOException e) {

                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Could not create directory for file storage");
                }
            }
            String fileName = UUID.randomUUID() + "." +
                    Objects.requireNonNull(file.getOriginalFilename())
                            .split("\\.")[1];
            Path destination = Paths.get(fileStoragePath.toString(), fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not store file " + file.getOriginalFilename());
        }
    }
    @Override
    public FileResponse uploadSingleFile(MultipartFile file, HttpServletRequest request) {
        String fileName = uploadFile(file);
        String fullImageUrl = generateUrl(fileName,request);
        return FileResponse.builder()
                .urlDownload(generateDownloadUrl(fileName,request))
                .type(file.getContentType())
                .size(file.getSize()/1024)
                .fileName(fileName)
                .url(fullImageUrl).build();
    }

    @Override
    public ArrayList<String> uploadMultipleFiles(MultipartFile[] files, HttpServletRequest request) {
        var fileUrls = new ArrayList<String>();
        for (var file : files) {
            FileResponse fileDTO = uploadSingleFile(file, request);
            fileUrls.add(fileDTO.getUrl());
        }
        return fileUrls;
    }
}
