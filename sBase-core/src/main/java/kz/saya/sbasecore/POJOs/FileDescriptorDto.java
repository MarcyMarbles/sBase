package kz.saya.sbasecore.POJOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link kz.saya.sbasecore.Entity.FileDescriptor}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FileDescriptorDto implements Serializable {
    private UUID id;
    private String label;
    private long size;
    private byte[] fileData;
    private String mimeType;
    private String extension;
    private String path;
    private String owner;
    private String description;
}