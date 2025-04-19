package com.example.todolist.Repos;

import com.example.todolist.Entity.FileDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, Integer> {
}