package uz.pdp.backendlink.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDTO {

    private String message;

    private int code;

    private List<FieldErrorDTO> fieldErrors;

    public ErrorDTO(String message, int code) {
        this.message = message;
        this.code = code;
    }

}
