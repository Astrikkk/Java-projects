package org.example.storage.impl;

import org.example.storage.StorageProperties;
import org.example.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }


    @Override
    public void init() throws IOException {
        if(!Files.exists(rootLocation))
            Files.createDirectory(rootLocation);
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        String randomFileName = java.util.UUID.randomUUID().toString() +"."+getFileExtension(file);
        Path destinationFile = this.rootLocation.resolve(randomFileName).normalize().toAbsolutePath();
        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return randomFileName;
    }

    private String getFileExtension(MultipartFile file) {
        String originFilename = file.getOriginalFilename();
        if(originFilename!=null && originFilename.contains(".")) {
            return originFilename.substring(originFilename.lastIndexOf(".")+1);
        }
        return "";
    }
}