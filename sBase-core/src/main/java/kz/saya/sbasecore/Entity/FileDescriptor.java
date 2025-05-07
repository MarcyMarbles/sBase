package kz.saya.sbasecore.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
public class FileDescriptor extends MappedSuperClass {
    private String label;
    private long size;
    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] fileData;
    private String mimeType;
    private String extension;
    private String path;
    private UUID owner;
    private String description;
}
