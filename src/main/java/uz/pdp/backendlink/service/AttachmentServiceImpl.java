package uz.pdp.backendlink.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.backendlink.dto.AttachmentDTO;
import uz.pdp.backendlink.entity.Attachment;
import uz.pdp.backendlink.entity.Design;
import uz.pdp.backendlink.mapper.AttachmentMapper;
import uz.pdp.backendlink.repository.AttachmentRepository;
import uz.pdp.backendlink.repository.DesignRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentMapper attachmentMapper;
    private final AttachmentRepository attachmentRepository;
    private final DesignRepository designRepository;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AttachmentDTO upload(MultipartFile image) {
        if (image.isEmpty()) return null;

        try {
            // 1. Unikal nom yaratish
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String fileName = UUID.randomUUID() + extension;

            // 2. Supabase yuklash URL manzili
            String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName;

            // 3. Headerlarni sozlash
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            headers.setContentType(MediaType.valueOf(image.getContentType()));

            // 4. Faylni Supabase-ga yuborish
            HttpEntity<byte[]> entity = new HttpEntity<>(image.getBytes(), headers);
            restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);

            // 5. Rasmni internetda ko'rish uchun PUBLIC URL yaratish
            String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + fileName;

            // 6. Ma'lumotlar bazasiga saqlash
            Attachment attachment = new Attachment();
            attachment.setOriginalFilename(originalFilename);
            attachment.setContentType(image.getContentType());
            attachment.setSize(image.getSize());
            attachment.setPath(publicUrl); // ENDI BU YERDA TO'LIQ URL SAQLANADI

            attachmentRepository.save(attachment);
            return attachmentMapper.toDTO(attachment);

        } catch (IOException e) {
            throw new RuntimeException("Supabase-ga yuklashda xatolik: " + e.getMessage());
        }
    }

    @Override
    public List<AttachmentDTO> uploadMultiple(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) return Collections.emptyList();
        List<AttachmentDTO> dtos = new ArrayList<>();
        for (MultipartFile image : images) {
            AttachmentDTO dto = upload(image);
            if (dto != null) dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ResponseEntity<Resource> download(Long id) {
        Attachment attachment = attachmentRepository.getByIdOrThrow(id);

        try {
            byte[] fileBytes = restTemplate.getForObject(attachment.getPath(), byte[].class);
            ByteArrayResource resource = new ByteArrayResource(fileBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + attachment.getOriginalFilename() + "\"")
                    .contentType(MediaType.parseMediaType(attachment.getContentType()))
                    .contentLength(fileBytes.length)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("Faylni yuklab olishda xatolik");
        }
    }


//    @Override
//    public ResponseEntity<Resource> download(Long id) {
//        // Supabase-da rasmlar public URL orqali to'g'ridan-to'g'ri ochiladi.
//        // Agar sizga baribir yuklab olish (download) API-si kerak bo'lsa:
//        Attachment attachment = attachmentRepository.getByIdOrThrow(id);
//
//        try {
//            byte[] fileBytes = restTemplate.getForObject(attachment.getPath(), byte[].class);
//            ByteArrayResource resource = new ByteArrayResource(fileBytes);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getOriginalFilename() + "\"")
//                    .contentType(MediaType.parseMediaType(attachment.getContentType()))
//                    .body(resource);
//        } catch (Exception e) {
//            throw new RuntimeException("Faylni yuklab olishda xatolik");
//        }
//    }

}


//
//@Service
//@RequiredArgsConstructor
//public class AttachmentServiceImpl implements AttachmentService {
//
//    private final AttachmentMapper attachmentMapper;
//    private final AttachmentRepository attachmentRepository;
//    private final String BASE_FOLDER = "uploads";
//
//    @Override
//    public AttachmentDTO upload(MultipartFile image) {
//        if (image.isEmpty())
//            return null;
//
//        try {
//            // 1. Papka borligini tekshirish (bo'lmasa yaratadi)
//            File folder = new File(BASE_FOLDER);
//            if (!folder.exists()) {
//                folder.mkdirs();
//            }
//
//            String originalFilename = image.getOriginalFilename();
//            String contentType = image.getContentType();
//            long size = image.getSize();
//
//            // Fayl formatini aniqlash (nuqta yo'q bo'lsa xato bermasligi uchun)
//            String extension = "";
//            if (originalFilename != null && originalFilename.contains(".")) {
//                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            }
//
//            // Unikal nom yaratish
//            String uuid = UUID.randomUUID() + extension;
//            Path path = Path.of(BASE_FOLDER, uuid);
//
//            // Faylni saqlash
//            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//            // Entity yaratish va saqlash
//            Attachment attachment = new Attachment();
//            attachment.setOriginalFilename(originalFilename);
//            attachment.setContentType(contentType);
//            attachment.setSize(size);
//            attachment.setPath(path.toString());
//
//            attachmentRepository.save(attachment);
//
//            return attachmentMapper.toDTO(attachment);
//
//        } catch (IOException e) {
//            throw new RuntimeException("Faylni saqlashda xatolik yuz berdi: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<AttachmentDTO> uploadMultiple(List<MultipartFile> images) {
//        if (images == null || images.isEmpty())
//            return Collections.emptyList();
//
//        List<AttachmentDTO> dtos = new ArrayList<>();
//        for (MultipartFile image : images) {
//            AttachmentDTO dto = upload(image);
//            if (dto != null) {
//                dtos.add(dto);
//            }
//        }
//        return dtos;
//    }
//
//    @Override
//    public ResponseEntity<Resource> download(Long id) {
//
//        Attachment attachment = attachmentRepository.getByIdOrThrow(id);
//
//        Path path = Path.of(attachment.getPath());
//        Resource resource = new FileSystemResource(path);
//
//        return ResponseEntity.status(200)
//                .header("Content-Disposition", "attachment; filename=\"%s\"".formatted(attachment.getOriginalFilename()))
//                .contentType(MediaType.parseMediaType(attachment.getContentType()))
//                .body(resource);
//    }
//
//}