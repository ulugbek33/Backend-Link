package uz.pdp.backendlink.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.backendlink.dto.AttachmentDTO;
import uz.pdp.backendlink.dto.DesignCreateDTO;
import uz.pdp.backendlink.dto.DesignDTO;
import uz.pdp.backendlink.entity.Attachment;
import uz.pdp.backendlink.entity.Design;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {AttachmentMapper.class})
public interface DesignMapper {

    @Mapping(source = "user.id", target = "userId")
    DesignDTO toDTO(Design design);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "user", ignore = true)
    Design toEntity(DesignCreateDTO designCreateDTO);

}
