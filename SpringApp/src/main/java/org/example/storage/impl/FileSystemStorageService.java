package org.example.storage.impl;

import net.coobird.thumbnailator.Thumbnails;
import org.example.service.FileSaveFormat;
import org.example.storage.StorageProperties;
import org.example.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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
    public void delete(String filename) {
        Path file = this.rootLocation.resolve(filename);
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Path getRootLocation() {
        return this.rootLocation;
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

    @Override
    public String saveImage(MultipartFile file, FileSaveFormat format) throws IOException {
        var bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        return saveBufferedImage(bufferedImage, format);
    }

    @Override
    public String saveImage(String fileUrl, FileSaveFormat format) throws IOException {
        try (InputStream inputStream = new URL(fileUrl).openStream()) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            return saveBufferedImage(bufferedImage, format);
        }
    }

    @Override
    public void deleteImage(String filename) throws IOException {
        // The deleteImage method delegates to the delete method, specialized for images
        Path imageToDelete = rootLocation.resolve(filename);
        if (Files.exists(imageToDelete)) {
            Files.delete(imageToDelete);
        } else {
            throw new IOException("Image not found: " + filename);
        }
    }

    private String saveBufferedImage(BufferedImage bufferedImage, FileSaveFormat format) throws IOException {
        String ext = format.name().toLowerCase();
        String randomFileName = UUID.randomUUID().toString()+"."+ext;
        int [] sizes = {32,150,300,600,1200};

        for (var size : sizes) {
            String fileSave = rootLocation.toString()+"/"+size+"_"+randomFileName;
            Thumbnails.of(bufferedImage).size(size, size).outputFormat(ext).toFile(fileSave);
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