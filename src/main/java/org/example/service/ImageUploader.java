package org.example.service;

import org.example.config.FileStorageProperties;
import org.example.controller.dto.ImageResponse;
import org.example.domain.ImageFile;
import org.example.domain.directory.Directory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageUploader {

    private final Path fileStorageLocation;
    private final DirectoryService directoryService;
    private final ImageService imageService;

    public ImageUploader(FileStorageProperties fileStorageProperties,
                         DirectoryService directoryService,
                         ImageService imageService) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath()
                .normalize();
        this.directoryService = directoryService;
        this.imageService = imageService;
    }

    @Transactional
    public ImageResponse uploadImage(String directoryPath, MultipartFile image) {
        createDirectory();

        ImageFile imageFile = ImageFile.from(image);
        try {
            String fileName = imageFile.fileName();
            Path targetLocation = fileStorageLocation.resolve(fileName);

            saveImagePathInDb(directoryPath, imageFile);

            Files.copy(imageFile.inputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new ImageResponse(convertToDownloadUri(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Error occurs on upload file step.");
        }
    }

    private void createDirectory() {
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not create the directory where the uploaded files will be stored." + fileStorageLocation);
        }
    }

    private void saveImagePathInDb(String directoryPath, ImageFile imageFile) {
        Directory imageSavedDirectory = directoryService.saveDirectory(directoryPath);
        imageService.saveImageInDirectory(imageFile, imageSavedDirectory);
    }

    private String convertToDownloadUri(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")  // TODO 실제로 저장할 도메인 prefix 설정
                .path(fileName)
                .toUriString();
    }
}
