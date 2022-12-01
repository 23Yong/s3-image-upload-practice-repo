package org.example.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.getFilenameExtension;

public class ImageFile {

    private static final Pattern VALIDATE_IMAGE_FILE_EXTENSION = Pattern.compile("^(png|jpeg|jpg|svg)$");

    private final String originalFileName;
    private final String contentType;
    private final String extension;
    private final byte[] imageBytes;

    private ImageFile(String originalFileName, String contentType, String extension, byte[] imageBytes) {
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.extension = extension;
        this.imageBytes = imageBytes;
    }

    public static ImageFile from(final MultipartFile multipartFile) {
        try {
            validateNullFile(multipartFile);
            validateEmptyFile(multipartFile);
            validateExtensionFile(multipartFile);

            return new ImageFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), getFilenameExtension(multipartFile.getOriginalFilename()), multipartFile.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalidate file" + multipartFile);
        }
    }

    private static void validateNullFile(final MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile)) {
            throw new IllegalArgumentException("File is null");
        }
    }

    private static void validateEmptyFile(final MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
    }

    private static void validateExtensionFile(final MultipartFile multipartFile) {
        String extension = getFilenameExtension(multipartFile.getOriginalFilename());

        if (!VALIDATE_IMAGE_FILE_EXTENSION.matcher(extension).matches()) {
            throw new IllegalArgumentException("Invalidate extension");
        }
    }

    private static void validateEmptyFileName(final MultipartFile multipartFile) {
        if (multipartFile.getOriginalFilename().isEmpty()) {
            throw new IllegalArgumentException("Invalidate file name");
        }
    }

    public String fileName() {
        return originalFileName;
    }

    public String extension() {
        return extension;
    }

    public InputStream inputStream() {
        return new ByteArrayInputStream(imageBytes);
    }
}
