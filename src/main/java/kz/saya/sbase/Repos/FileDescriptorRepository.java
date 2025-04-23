package kz.saya.sbase.Repos;

import kz.saya.sbase.Entity.FileDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, UUID> {
}