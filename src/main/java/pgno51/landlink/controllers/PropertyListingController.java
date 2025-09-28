package pgno51.landlink.controllers;

import org.springframework.web.bind.annotation.*;
import pgno51.landlink.models.PropertyListing;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/listings")
public class PropertyListingController {

    private List<PropertyListing> listings = new ArrayList<>(); // In-memory storage for demonstration

    @PostMapping("/create")
    public PropertyListing createListing(@RequestBody PropertyListing listing) {
        listing.setId((long) (listings.size() + 1)); // Assign a simple ID
        listing.setStatus("Pending Approval");
        listings.add(listing);
        return listing;
    }

    @GetMapping
    public List<PropertyListing> getAllListings() {
        return listings;
    }

    @PutMapping("/{id}")
    public PropertyListing updateListing(@PathVariable Long id, @RequestBody PropertyListing updatedListing) {
        for (int i = 0; i < listings.size(); i++) {
            if (listings.get(i).getId().equals(id)) {
                updatedListing.setId(id);
                listings.set(i, updatedListing);
                return updatedListing;
            }
        }
        return null; // Or throw an exception if not found
    }

    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable Long id) {
        listings.removeIf(listing -> listing.getId().equals(id));
    }
}
