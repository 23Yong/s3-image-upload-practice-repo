package org.example.controller.dto;

import lombok.Data;

@Data
public class UploadFileResponse {

    private String fileName;
    private String fileDownloadUrl;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileDownloadUrl, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUrl = fileDownloadUrl;
        this.fileType = fileType;
        this.size = size;
    }
}
