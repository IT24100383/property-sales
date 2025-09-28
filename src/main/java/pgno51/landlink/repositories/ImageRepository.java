package pgno51.landlink.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pgno51.landlink.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
