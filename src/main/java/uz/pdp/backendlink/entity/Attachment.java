package uz.pdp.backendlink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import uz.pdp.backendlink.entity.template.AbsLongEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Attachment extends AbsLongEntity {

    @Column(columnDefinition = "text")
    private String originalFilename;

    private String contentType;

    private Long size;

    @Column(columnDefinition = "text")
    private String path;

}
