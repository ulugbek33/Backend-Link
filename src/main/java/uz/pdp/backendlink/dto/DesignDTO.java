package uz.pdp.backendlink.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link uz.pdp.backendlink.entity.Design}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String title;

    private String description;

    private String type;

    private String resourceLink;

    private Long userId;

    private List<AttachmentDTO> images;
}