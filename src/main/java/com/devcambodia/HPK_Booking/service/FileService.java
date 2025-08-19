package com.devcambodia.HPK_Booking.service;

import com.devcambodia.HPK_Booking.dto.res.FileResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public interface FileService {
    FileResponse uploadSingleFile(MultipartFile file, HttpServletRequest request);
    ArrayList<String> uploadMultipleFiles(MultipartFile[] files, HttpServletRequest request);
}
