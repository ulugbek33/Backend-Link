package uz.pdp.backendlink.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDTO {

    private String username;

    @NotBlank(message = "Parol bo'sh bo'lmasligi kerak")
    private String password;

    @NotBlank(message = "Email bo'sh bo'lmasligi kerak")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Iltimos, haqiqiy email manzilini kiriting"
    )
    private String email;
}
