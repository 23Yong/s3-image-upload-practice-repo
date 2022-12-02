package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.ImageFile;
import org.example.domain.directory.Directory;
import org.example.domain.image.Image;
import org.example.domain.image.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void saveImageInDirectory(ImageFile imageFile, Directory imageSavedDirectory) {
        Image image = Image.of(imageFile.fileName(), imageFile.extension(), imageSavedDirectory);
        imageRepository.save(image);
    }
}
