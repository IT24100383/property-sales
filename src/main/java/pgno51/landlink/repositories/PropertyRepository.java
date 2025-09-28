package pgno51.landlink.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pgno51.landlink.models.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
