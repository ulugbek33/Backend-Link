package uz.pdp.backendlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link uz.pdp.backendlink.entity.Attachment}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO implements Serializable {

    private Long id;

    private String originalFilename;

    private String contentType;

    private Long size;

    private String path;

}
