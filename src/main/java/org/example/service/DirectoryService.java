package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.directory.Directory;
import org.example.domain.directory.DirectoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class DirectoryService {

    private static final String FILE_SEPARATOR = "file.separator";

    private final DirectoryRepository directoryRepository;

    @Transactional
    public Directory saveDirectory(String directoryPath) {
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
}
