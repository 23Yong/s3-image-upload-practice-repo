package org.example.service;

import org.example.controller.dto.ImageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ImageUploaderTest {

    @Autowired
    private ImageUploader imageUploader;

    private MultipartFile image;

    @BeforeEach
    void setUp() {
        image = new MockMultipartFile("image",
                "test.jpg",
                "image/jpg",
                "content".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void 이미지_업로드_후_이미지_경로를_반환한다() {
        // given
        ImageResponse expected = new ImageResponse("http://localhost/downloadFile/test.jpg");

        // when
        ImageResponse actual = imageUploader.uploadImage("/1/2/3/", image);

        // then
        assertThat(actual.getImagePath()).isEqualTo(expected.getImagePath());
    }
}