package uz.pdp.backendlink.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.backendlink.dto.UserDTO;
import uz.pdp.backendlink.entity.User;
import uz.pdp.backendlink.exceptions.EntityNotFoundException;
import uz.pdp.backendlink.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserDTO readAll() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null)
            throw new EntityNotFoundException("Bunday user ro'yhatda topilmadi" + user.getId(), HttpStatus.NOT_FOUND);

        return userMapper.toDTO(user);

    }

}
