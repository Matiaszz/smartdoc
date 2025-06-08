package dev.matias.smartDoc.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name="documents")
@EqualsAndHashCode
@AllArgsConstructor
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DocType type;
    private String name;
    private double size;

    @Lob
    private byte[] data;

    public Document() {

    }

    public void setData(byte[] data) {
        this.data = data;
        this.size = (data != null) ? data.length : 0;
    }
}
