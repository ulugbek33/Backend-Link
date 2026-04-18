package uz.pdp.backendlink.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.backendlink.dto.UserDTO;
import uz.pdp.backendlink.repository.UserRepository;
import uz.pdp.backendlink.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://tastelab-tawny.vercel.app"})
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserDTO getCurrentUser() {
        return userService.readAll();
    }

}
