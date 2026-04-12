package uz.pdp.backendlink.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.backendlink.dto.UserDTO;
import uz.pdp.backendlink.dto.security.RegisterDTO;
import uz.pdp.backendlink.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(RegisterDTO registerDTO);

    User toEntity(UserDTO userDTO);
}