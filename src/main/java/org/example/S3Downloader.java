package org.example;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Downloader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String downloadFile(String dirName, String fileName) {
        String filePath = amazonS3Client.getUrl(bucket, dirName + "/" + fileName).toString();

        log.info("[FilePath] : {}", filePath);

        return filePath;
    }
}
