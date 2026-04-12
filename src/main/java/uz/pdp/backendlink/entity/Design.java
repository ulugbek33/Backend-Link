package uz.pdp.backendlink.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.backendlink.entity.template.AbsLongEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Design extends AbsLongEntity {

    private String title;

    @Column(columnDefinition = "text")
    private String description;

    private String type;

    private String resourceLink;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "design_id")
    private List<Attachment> images;

}
