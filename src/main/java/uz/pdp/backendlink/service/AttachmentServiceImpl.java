package uz.pdp.backendlink.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.backendlink.dto.AttachmentDTO;
import uz.pdp.backendlink.entity.Attachment;
import uz.pdp.backendlink.exceptions.EntityNotFoundException;
import uz.pdp.backendlink.mapper.AttachmentMapper;
import uz.pdp.backendlink.repository.AttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentMapper attachmentMapper;
    private final AttachmentRepository attachmentRepository;
    private final String BASE_FOLDER = "uploads";

    @Override
    public AttachmentDTO upload(MultipartFile image) {
        if (image.isEmpty())
            return null;

        try {
            // 1. Papka borligini tekshirish (bo'lmasa yaratadi)
            File folder = new File(BASE_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String originalFilename = image.getOriginalFilename();
            String contentType = image.getContentType();
            long size = image.getSize();

            // Fayl formatini aniqlash (nuqta yo'q bo'lsa xato bermasligi uchun)
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // Unikal nom yaratish
            String uuid = UUID.randomUUID() + extension;
            Path path = Path.of(BASE_FOLDER, uuid);

            // Faylni saqlash
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Entity yaratish va saqlash
            Attachment attachment = new Attachment();
            attachment.setOriginalFilename(originalFilename);
            attachment.setContentType(contentType);
            attachment.setSize(size);
            attachment.setPath(path.toString());

            attachmentRepository.save(attachment);

            return attachmentMapper.toDTO(attachment);

        } catch (IOException e) {
            throw new RuntimeException("Faylni saqlashda xatolik yuz berdi: " + e.getMessage());
        }
    }

    @Override
    public List<AttachmentDTO> uploadMultiple(List<MultipartFile> images) {
        if (images == null || images.isEmpty())
            return Collections.emptyList();

        List<AttachmentDTO> dtos = new ArrayList<>();
        for (MultipartFile image : images) {
            AttachmentDTO dto = upload(image);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Override
    public ResponseEntity<Resource> download(Long id) {

        Attachment attachment = attachmentRepository.getByIdOrThrow(id);

        Path path = Path.of(attachment.getPath());
        Resource resource = new FileSystemResource(path);

        return ResponseEntity.status(200)
                .header("Content-Disposition", "attachment; filename=\"%s\"".formatted(attachment.getOriginalFilename()))
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .body(resource);
    }

}