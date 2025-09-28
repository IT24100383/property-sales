package pgno51.landlink.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pgno51.landlink.models.Image;
import pgno51.landlink.models.Property;
import pgno51.landlink.repositories.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Value("${app.storage.images}")
    private String imagesDir;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image store(MultipartFile file, Property property) throws IOException {
        Files.createDirectories(Paths.get(imagesDir));
        String ext = extractExt(file.getOriginalFilename());
        String newName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path target = Paths.get(imagesDir).resolve(newName);
        file.transferTo(target);

        Image img = new Image();
        img.setFileName(newName);
        img.setFilePath(target.toAbsolutePath().toString());
        img.setProperty(property);
        return imageRepository.save(img);
    }

    private String extractExt(String name) {
        if (name == null) return "";
        int i = name.lastIndexOf('.')
;        return i >= 0 ? name.substring(i+1) : "";
    }
}
