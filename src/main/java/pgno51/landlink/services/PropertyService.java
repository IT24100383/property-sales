package pgno51.landlink.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pgno51.landlink.models.Property;
import pgno51.landlink.repositories.PropertyRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<Property> findAll() { return propertyRepository.findAll(); }
    public Optional<Property> findById(Long id) { return propertyRepository.findById(id); }

    @Transactional
    public Property create(Property p) {
        validate(p);
        p.setId(null);
        if (!StringUtils.hasText(p.getStatus())) p.setStatus("DRAFT");
        return propertyRepository.save(p);
    }

    @Transactional
    public Property update(Long id, Property updated) {
        validate(updated);
        Property existing = propertyRepository.findById(id).orElseThrow();
        existing.setTitle(updated.getTitle());
        existing.setLocation(updated.getLocation());
        existing.setSize(updated.getSize());
        existing.setPrice(updated.getPrice());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        return propertyRepository.save(existing);
    }

    public void delete(Long id) { propertyRepository.deleteById(id); }

    private void validate(Property p) {
        if (!StringUtils.hasText(p.getTitle())) throw new IllegalArgumentException("Title required");
        if (!StringUtils.hasText(p.getLocation())) throw new IllegalArgumentException("Location required");
        if (p.getSize() == null || p.getSize() <= 0) throw new IllegalArgumentException("Size must be > 0");
        if (p.getPrice() == null || p.getPrice().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Price must be > 0");
        if (!StringUtils.hasText(p.getSellerId())) throw new IllegalArgumentException("Seller required");
    }
}
