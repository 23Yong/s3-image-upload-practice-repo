package org.example.domain.directory;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "directory", indexes = {
        @Index(columnList = "directory_name")
})
@Entity
public class Directory {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "directory_name", nullable = false)
    private String directoryName;

    private Directory(String directoryName) {
        this.directoryName = directoryName;
    }

    public static Directory of(String directoryName) {
        return new Directory(directoryName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Directory directory = (Directory) o;
        return Objects.equals(id, directory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
