package uz.pdp.backendlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageableDTO {

    private Integer page;

    private Integer size;

    private Long totalElements;

    private boolean hasNext;

    private boolean hasPrevious;

    private List<?> objects;

}
