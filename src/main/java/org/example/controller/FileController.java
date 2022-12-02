package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.dto.ImageResponse;
import org.example.service.ImageUploader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {

    private final ImageUploader imageUploader;

    @PostMapping("/upload")
    public ImageResponse upload(
            @RequestPart(name = "directoryPath") String directoryPath,
            @RequestPart(name = "file") MultipartFile file) {
        return imageUploader.uploadImage(directoryPath, file);
    }
}
