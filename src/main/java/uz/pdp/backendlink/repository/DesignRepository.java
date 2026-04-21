package uz.pdp.backendlink.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import uz.pdp.backendlink.entity.Attachment;
import uz.pdp.backendlink.entity.Design;
import uz.pdp.backendlink.entity.User;
import uz.pdp.backendlink.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface DesignRepository extends JpaRepository<Design, Long> {

    default Design getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Design not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    Page<Design> findAllByUser(User user, Pageable pageable);

    List<Design> getByUser(User user);

    @Query("SELECT d FROM Design d WHERE " +
            "LOWER(d.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(d.type) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Design> searchDesigns(@Param("search") String search);

}