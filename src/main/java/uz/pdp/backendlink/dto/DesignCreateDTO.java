package uz.pdp.backendlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignCreateDTO {

    private String title;
    private String description;
    private String type;
    private String resourceLink;
    private List<MultipartFile> images;

}