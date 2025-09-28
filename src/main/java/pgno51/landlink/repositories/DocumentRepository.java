package pgno51.landlink.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pgno51.landlink.models.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
