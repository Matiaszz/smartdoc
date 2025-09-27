package dev.matias.smartDoc.Domain.Document;

import dev.matias.smartDoc.Domain.Document.ValueObjects.DocType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="documents")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DocType type;

    @Column(unique = true)
    private String blobName;

    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void setBlobName() {
        this.blobName = this.id + "_" + this.name;
    }

    public Document(UUID id, String name){
        this.id = id;
        this.name = name;
    }
}
