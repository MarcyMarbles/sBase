package kz.saya.sbasecore.Service;

import kz.saya.sbasecore.Entity.FileDescriptor;
import kz.saya.sbasecore.POJOs.FileDescriptorDto;
import kz.saya.sbasecore.Repositories.FileDescriptorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileDescriptorService {

    private final FileDescriptorRepository fileDescriptorRepository;

    @Transactional
    public FileDescriptor saveFileDescriptor(FileDescriptor fileDescriptor) {
        return fileDescriptorRepository.save(fileDescriptor);
    }

    @Transactional
    public FileDescriptor saveFile(FileDescriptorDto file) throws IOException {
        FileDescriptor fileDescriptor = new FileDescriptor();
        fileDescriptor.setLabel(file.getLabel());
        fileDescriptor.setMimeType(file.getMimeType());
        fileDescriptor.setSize(file.getSize());
        fileDescriptor.setExtension(file.getExtension());
        fileDescriptor.setFileData(file.getFileData());
        fileDescriptor.setOwner(file.getOwner());
        fileDescriptor.setDescription(file.getDescription());

        return fileDescriptorRepository.save(fileDescriptor);
    }

    public Optional<FileDescriptor> getFileDescriptor(UUID id) {
        return fileDescriptorRepository.findById(id);
    }

    public List<FileDescriptor> getAllFileDescriptors() {
        return fileDescriptorRepository.findAll();
    }

    public List<FileDescriptor> getFileDescriptorsByOwner(UUID owner) {
        return fileDescriptorRepository.findByOwner(owner);
    }

    @Transactional
    public Optional<FileDescriptor> updateFileDescriptor(UUID id, FileDescriptor updatedFileDescriptor) {
        return fileDescriptorRepository.findById(id)
                .map(existingFile -> {
                    existingFile.setLabel(updatedFileDescriptor.getLabel());
                    existingFile.setDescription(updatedFileDescriptor.getDescription());
                    existingFile.setMimeType(updatedFileDescriptor.getMimeType());
                    existingFile.setExtension(updatedFileDescriptor.getExtension());
                    return fileDescriptorRepository.save(existingFile);
                });
    }

    @Transactional
    public boolean softDeleteFileDescriptor(UUID id) {
        return fileDescriptorRepository.findById(id)
                .map(fileDescriptor -> {
                    fileDescriptor.setDeleted_ts(OffsetDateTime.now());
                    fileDescriptorRepository.save(fileDescriptor);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void deleteFileDescriptor(UUID id) {
        fileDescriptorRepository.deleteById(id);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
} 