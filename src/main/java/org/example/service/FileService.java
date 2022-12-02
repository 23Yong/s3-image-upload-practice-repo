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
import java.util.regex.Pattern;

@Service
public class FileService {

    private static final String FILE_SEPARATOR = "file.separator";

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
    public String storeFile(String directoryPath, MultipartFile image) {
        ImageFile imageFile = ImageFile.from(image);

        try {
            String fileName = imageFile.fileName();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Directory savedDirectory = saveDirectoryPath(directoryPath);
            saveImage(imageFile, savedDirectory);

            Files.copy(imageFile.inputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    private Directory saveDirectoryPath(String directoryPath) {
        String[] tokens = directoryPath.split(Pattern.quote(System.getProperty(FILE_SEPARATOR)));

        Directory parentDirectory = Directory.of(tokens[0]);
        directoryRepository.save(parentDirectory);

        for (int i = 1; i < tokens.length; i++) {
            Directory childDirectory = Directory.of(tokens[i]);
            parentDirectory.addChild(childDirectory);
            directoryRepository.save(childDirectory);

            parentDirectory = childDirectory;
        }

        return parentDirectory;
    }

    private void saveImage(ImageFile imageFile, Directory directory) {
        imageRepository.save(Image.of(imageFile.fileName(), imageFile.extension(), directory));
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
