package uz.pdp.backendlink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import uz.pdp.backendlink.entity.Attachment;
import uz.pdp.backendlink.exceptions.EntityNotFoundException;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    default Attachment getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Attachment not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    Long id(Long id);
}