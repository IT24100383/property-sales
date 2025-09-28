package pgno51.landlink.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pgno51.landlink.models.Document;
import pgno51.landlink.models.Property;
import pgno51.landlink.services.DocumentService;
import pgno51.landlink.services.ImageService;
import pgno51.landlink.services.PropertyService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/listings")
public class PropertyController {
    private final PropertyService propertyService;
    private final ImageService imageService;
    private final DocumentService documentService;

    public PropertyController(PropertyService propertyService, ImageService imageService, DocumentService documentService) {
        this.propertyService = propertyService;
        this.imageService = imageService;
        this.documentService = documentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("properties", propertyService.findAll());
        return "property/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("property", new Property());
        return "property/create";
    }

    @PostMapping
    public String create(@ModelAttribute("property") @Valid Property property,
                         BindingResult result,
                         @RequestParam(value = "images", required = false) List<MultipartFile> images,
                         @RequestParam(value = "document", required = false) MultipartFile document,
                         Model model) {
        if (result.hasErrors()) {
            return "property/create";
        }
        try {
            Property saved = propertyService.create(property);
            if (images != null) {
                for (MultipartFile img : images) {
                    if (img != null && !img.isEmpty()) {
                        imageService.store(img, saved);
                    }
                }
            }
            if (document != null && !document.isEmpty()) {
                documentService.encryptAndSave(document, saved);
            }
            return "redirect:/listings";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Property p = propertyService.findById(id).orElse(null);
        if (p == null) return "error";
        model.addAttribute("property", p);
        return "property/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Property p = propertyService.findById(id).orElse(null);
        if (p == null) return "error";
        model.addAttribute("property", p);
        return "property/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("property") @Valid Property property,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            return "property/edit";
        }
        try {
            propertyService.update(id, property);
            return "redirect:/listings/" + id;
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        propertyService.delete(id);
        return "redirect:/listings";
    }
}
