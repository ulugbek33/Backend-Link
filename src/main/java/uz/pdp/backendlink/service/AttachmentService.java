package uz.pdp.backendlink.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.backendlink.dto.AttachmentDTO;

import java.util.List;

public interface AttachmentService {

    AttachmentDTO upload(MultipartFile image);

    List<AttachmentDTO> uploadMultiple(List<MultipartFile> images);

    ResponseEntity<Resource> download(Long id);
}
