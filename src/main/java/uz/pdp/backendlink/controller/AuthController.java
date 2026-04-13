package uz.pdp.backendlink.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.pdp.backendlink.dto.security.LoginDTO;
import uz.pdp.backendlink.dto.security.RegisterDTO;
import uz.pdp.backendlink.dto.security.TokenDTO;
import uz.pdp.backendlink.service.security.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/verify")
    public TokenDTO verify(@RequestParam String refreshToken) {
        return authService.verifyToken(refreshToken);
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @PostMapping("/register")
    public TokenDTO register(@RequestBody @Valid RegisterDTO registerDTO) {
        return authService.register(registerDTO);
    }

}
