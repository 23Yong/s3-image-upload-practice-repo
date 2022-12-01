package org.example.service;

import org.example.config.FileStorageProperties;
import org.example.domain.ImageFile;
import org.example.domain.directory.Directory;
import org.example.domain.directory.DirectoryRepository;
import org.example.domain.image.Image;
import org.example.domain.image.ImageRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final DirectoryRepository directoryRepository;
    private final ImageRepository imageRepository;

    public FileService(FileStorageProperties properties, DirectoryRepository directoryRepository, ImageRepository imageRepository) {
        fileStorageLocation = Paths.get(properties.getUploadDir())
                .toAbsolutePath().normalize();
        this.directoryRepository = directoryRepository;
        this.imageRepository = imageRepository;

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not create the directory where the uploaded files will be stored." + fileStorageLocation);
        }
    }

    @Transactional
    public String storeFile(String directoryName, MultipartFile image) {
        ImageFile imageFile = ImageFile.from(image);

        try {
            String fileName = imageFile.fileName();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Directory savedDirectory = directoryRepository.save(Directory.of(directoryName));
            imageRepository.save(Image.of(fileName, imageFile.extension(), savedDirectory));

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
