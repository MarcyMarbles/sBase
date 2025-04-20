package kz.saya.sbase.Service;

import kz.saya.sbase.Entity.FileDescriptor;
import kz.saya.sbase.Repos.FileDescriptorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

@Service
public class FileDescriptorService {

    @Autowired
    private FileDescriptorRepository fileDescriptorRepo;

    @Transactional
    public FileDescriptor saveFile(FileDescriptor fileDescriptor) {
        return fileDescriptorRepo.save(fileDescriptor);
        // WHY? since byte[] is lob type, it can't be saved with auto-commit
    }

    @Transactional
    public FileDescriptor saveFile(MultipartFile file) {
        FileDescriptor fileDescriptor = new FileDescriptor();
        fileDescriptor.setLabel(file.getOriginalFilename());
        fileDescriptor.setMimeType(file.getContentType());
        fileDescriptor.setSize(file.getSize());
        fileDescriptor.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1));
        try {
            fileDescriptor.setFileData(file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file data", e);
        }
        return saveFile(fileDescriptor);
    }

    @Transactional
    public void softDeleteFile(FileDescriptor fileDescriptor) {
        fileDescriptor.setDeleted_ts(OffsetDateTime.now());
        fileDescriptorRepo.save(fileDescriptor);
    }
}
