package uz.pdp.backendlink.service.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import uz.pdp.backendlink.dto.security.LoginDTO;
import uz.pdp.backendlink.dto.security.RegisterDTO;
import uz.pdp.backendlink.dto.security.TokenDTO;
import uz.pdp.backendlink.entity.User;

public interface AuthService extends UserDetailsService {

    TokenDTO verifyToken(String refreshToken);

    TokenDTO login(LoginDTO loginDTO);

    TokenDTO register(RegisterDTO registerDTO);

}
