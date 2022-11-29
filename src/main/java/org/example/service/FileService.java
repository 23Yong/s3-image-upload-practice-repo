package org.example.service;

import org.example.config.FileStorageProperties;
import org.example.domain.ImageFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private final Path fileStorageLocation;

    public FileService(FileStorageProperties properties) {
        fileStorageLocation = Paths.get(properties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not create the directory where the uploaded files will be stored." + fileStorageLocation);
        }
    }

    public String storeFile(MultipartFile image) {
        ImageFile imageFile = ImageFile.from(image);

        try {
            String fileName = imageFile.fileName();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(imageFile.inputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            }
            throw new FileNotFoundException();
        } catch (MalformedURLException | FileNotFoundException e) {
            throw new RuntimeException("File not found " + fileName);
        }
    }
}
