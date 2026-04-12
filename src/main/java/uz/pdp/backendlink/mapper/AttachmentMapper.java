package uz.pdp.backendlink.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.backendlink.dto.AttachmentDTO;
import uz.pdp.backendlink.entity.Attachment;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttachmentMapper {

    AttachmentDTO toDTO(Attachment attachment);

    Attachment toEntity(AttachmentDTO attachmentDTO);

}
