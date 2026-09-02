package com.example.suggestion_system;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    public FileStorageService(){
        try{
            if(!Files.exists(root)){
                Files.createDirectory(root);
            }
        }catch(IOException e){
            throw new RuntimeException("Could not initialize folder for uploads!");
        }
    }

    public String save(MultipartFile file){
        try{
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(fileName));
            return fileName;
        }catch(Exception e){
            throw new RuntimeException("Could not store the file. Error: "+e.getMessage());
        }
    }
}
