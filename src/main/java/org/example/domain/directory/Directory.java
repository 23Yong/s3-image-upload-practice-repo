package org.example.domain.directory;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@ToString(callSuper = true)
@Getter
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

    @Column(name = "parent_directory_id", updatable = false)
    private Long parentDirectoryId;

    @ToString.Exclude
    @OneToMany(mappedBy = "parentDirectoryId", cascade = CascadeType.ALL)
    private Set<Directory> childDirectories = new LinkedHashSet<>();

    private Directory(String directoryName, Long parentDirectoryId) {
        this.directoryName = directoryName;
        this.parentDirectoryId = parentDirectoryId;
    }

    public void setParentDirectoryId(Long parentDirectoryId) {
        this.parentDirectoryId = parentDirectoryId;
    }

    public static Directory of(String directoryName) {
        return new Directory(directoryName, null);
    }

    public void addChild(Directory child) {
        child.setParentDirectoryId(this.getId());
        this.getChildDirectories().add(child);
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
