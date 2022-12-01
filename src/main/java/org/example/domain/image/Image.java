package org.example.domain.image;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.domain.directory.Directory;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Column(length = 10, nullable = false)
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id", nullable = false)
    private Directory directory;

    private Image(String imageName, String extension, Directory directory) {
        this.imageName = imageName;
        this.extension = extension;
        this.directory = directory;
    }

    public static Image of(String imageName, String extension, Directory directory) {
        return new Image(imageName, extension, directory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image image = (Image) o;
        return Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
