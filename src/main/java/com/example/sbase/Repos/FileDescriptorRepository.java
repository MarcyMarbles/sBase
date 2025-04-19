package com.example.sbase.Repos;

import com.example.sbase.Entity.FileDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, Integer> {
}