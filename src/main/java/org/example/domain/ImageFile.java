package org.example.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.getFilenameExtension;

public class ImageFile {

    private static final Pattern VALIDATE_IMAGE_FILE_EXTENSION = Pattern.compile("^(png|jpeg|jpg|svg)$");

    private final String originalFileName;
    private final String contentType;
    private final String extension;
    private final byte[] imageBytes;

    public ImageFile(String originalFileName, String contentType, String extension, byte[] imageBytes) {
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.extension = extension;
        this.imageBytes = imageBytes;
    }

    public static ImageFile from(final MultipartFile multipartFile) {
        try {
            return new ImageFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), getFilenameExtension(multipartFile.getOriginalFilename()), multipartFile.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalidate file" + multipartFile);
        }
    }

    public String fileName() {
        return originalFileName;
    }

    public InputStream inputStream() {
        return new ByteArrayInputStream(imageBytes);
    }
}
