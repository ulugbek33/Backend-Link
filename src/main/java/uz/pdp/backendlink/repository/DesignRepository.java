package uz.pdp.backendlink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import uz.pdp.backendlink.entity.Attachment;
import uz.pdp.backendlink.entity.Design;
import uz.pdp.backendlink.exceptions.EntityNotFoundException;

public interface DesignRepository extends JpaRepository<Design, Long> {

    default Design getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Design not found with id: " + id, HttpStatus.NOT_FOUND));
    }

}