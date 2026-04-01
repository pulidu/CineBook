package lk.ijse.cinebook.service.impl;

import lk.ijse.cinebook.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    @Value("${upload.path:./uploads/posters/}")
    private String uploadPath;

    @Override
    public String uploadPoster(MultipartFile file) throws IOException {
        // Create directory if not exists
        Path path = Paths.get(uploadPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = path.resolve(filename);
        Files.write(filePath, file.getBytes());

        // Return URL for frontend
        return "/uploads/posters/" + filename;
    }

    @Override
    public boolean deletePoster(String imageUrl) {
        try {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                Path filePath = Paths.get(uploadPath, filename);
                return Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}