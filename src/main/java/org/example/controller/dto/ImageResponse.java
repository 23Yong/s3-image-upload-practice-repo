package org.example.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageResponse {

    private String imagePath;

    public ImageResponse(final String imagePath) {
        this.imagePath = imagePath;
    }
}
