package uz.pdp.backendlink.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.backendlink.dto.DesignCreateDTO;
import uz.pdp.backendlink.dto.DesignDTO;
import uz.pdp.backendlink.dto.PageableDTO;
import uz.pdp.backendlink.service.AttachmentService;
import uz.pdp.backendlink.service.DesignService;

import java.util.List;

@RestController
@RequestMapping("/api/design")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DesignController {

    private final DesignService designService;
    private final AttachmentService attachmentService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public PageableDTO getDesign(@Parameter(description = "Page number", example = "0")
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @Parameter(description = "Page size", example = "10")
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        return designService.getAll(page, size);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public DesignDTO getDesignById(@PathVariable Long id) {
        return designService.getDesignById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        return attachmentService.download(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createDesign(@ModelAttribute DesignCreateDTO designDTO) {

        try {
            designService.saveDesign(designDTO);
            return ResponseEntity.ok("Dizayn muvaffaqiyatli saqlandi!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Xatolik: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DesignDTO editDesign(@PathVariable Long id,
                                @Valid @ModelAttribute DesignCreateDTO designCreateDTO) {
        return designService.edit(id, designCreateDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDesign(@PathVariable Long id) {
        designService.delete(id);
        return ResponseEntity.ok("Deleted design with id: " + id + " successfully!");
    }

}
