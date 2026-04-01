package lk.ijse.cinebook.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
    String uploadPoster(MultipartFile file) throws IOException;
    boolean deletePoster(String imageUrl);
}
