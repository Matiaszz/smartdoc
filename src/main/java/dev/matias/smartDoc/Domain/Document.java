package dev.matias.smartDoc.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="documents")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
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

    public void setBlobName() {
        this.blobName = this.id + "_" + this.name;
    }
}
