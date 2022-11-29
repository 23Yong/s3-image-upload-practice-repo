package org.example.domain;

public class ImageFile {

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
}
