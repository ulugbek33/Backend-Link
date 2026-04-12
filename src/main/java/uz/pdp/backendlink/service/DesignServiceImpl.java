package uz.pdp.backendlink.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.backendlink.dto.AttachmentDTO;
import uz.pdp.backendlink.dto.DesignCreateDTO;
import uz.pdp.backendlink.dto.DesignDTO;
import uz.pdp.backendlink.entity.Attachment;
import uz.pdp.backendlink.entity.Design;
import uz.pdp.backendlink.entity.User;
import uz.pdp.backendlink.mapper.DesignMapper;
import uz.pdp.backendlink.repository.AttachmentRepository;
import uz.pdp.backendlink.repository.DesignRepository;
import uz.pdp.backendlink.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignServiceImpl implements DesignService {

    private final DesignRepository designRepository;
    private final AttachmentService attachmentService;
    private final AttachmentRepository attachmentRepository;
    private final DesignMapper designMapper;
    private final UserRepository userRepository;

    @Override
    public DesignDTO getDesignById(Long id) {

        Design design = designRepository.getByIdOrThrow(id);

        return designMapper.toDTO(design);
    }

    @Override
    @Transactional
    public void saveDesign(DesignCreateDTO designCreateDTO) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        Design design = designMapper.toEntity(designCreateDTO);
        design.setUser(user);

        if (designCreateDTO.getImages() != null && !designCreateDTO.getImages().isEmpty()) {
            List<AttachmentDTO> attachmentDTOS = attachmentService.uploadMultiple(designCreateDTO.getImages());

            List<Attachment> attachments = attachmentDTOS.stream()
                    .map(a -> attachmentRepository.getByIdOrThrow(a.getId()))
                    .collect(Collectors.toList());

            design.setImages(attachments);
        }
        designRepository.save(design);
    }

}