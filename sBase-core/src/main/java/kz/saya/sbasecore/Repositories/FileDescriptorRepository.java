package kz.saya.sbasecore.Repositories;

import kz.saya.sbasecore.Entity.FileDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, UUID> {
}
