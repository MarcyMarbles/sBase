package kz.saya.sbasecore.Repositories;

import kz.saya.sbasecore.Entity.FileDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, UUID> {
    List<FileDescriptor> findByOwner(UUID owner);
}
