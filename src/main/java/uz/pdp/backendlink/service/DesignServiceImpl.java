package uz.pdp.backendlink.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.backendlink.dto.AttachmentDTO;
import uz.pdp.backendlink.dto.DesignCreateDTO;
import uz.pdp.backendlink.dto.DesignDTO;
import uz.pdp.backendlink.dto.PageableDTO;
import uz.pdp.backendlink.entity.Attachment;
import uz.pdp.backendlink.entity.Design;
import uz.pdp.backendlink.entity.User;
import uz.pdp.backendlink.entity.template.AbsLongEntity;
import uz.pdp.backendlink.exceptions.EntityNotFoundException;
import uz.pdp.backendlink.mapper.DesignMapper;
import uz.pdp.backendlink.repository.AttachmentRepository;
import uz.pdp.backendlink.repository.DesignRepository;
import uz.pdp.backendlink.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public PageableDTO getAll(int page, int size) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null)
            throw new EntityNotFoundException("Bunday user mavjud emas " + user.getUsername(), HttpStatus.NOT_FOUND);

        Sort sort = Sort.by(AbsLongEntity.Fields.id).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designPage = designRepository.findAll(pageable);

        return new PageableDTO(
                designPage.getTotalPages(),
                designPage.getSize(),
                designPage.getTotalElements(),
                designPage.hasNext(),
                designPage.hasPrevious(),
                designMapper.toDTO(designPage.getContent())
        );

    }

    @Override
    @Transactional
    public DesignDTO edit(Long id, DesignCreateDTO designCreateDTO) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Design design = designRepository.getByIdOrThrow(id);

        if (!design.getUser().getId().equals(user.getId()))
            throw new SecurityException(" : Bu dizayin sizga tegishli emas shuning uchun o'zgartirolmaysiz !!!");

        design.setUser(user);
        design.setType(designCreateDTO.getType());
        design.setTitle(designCreateDTO.getTitle());
        design.setDescription(designCreateDTO.getDescription());
        design.setResourceLink(designCreateDTO.getResourceLink());

        if (designCreateDTO.getImages() != null && !designCreateDTO.getImages().isEmpty()) {

            if (design.getImages() != null && !design.getImages().isEmpty()) {
                for (Attachment oldAttachment : design.getImages()) {
                    deletePhysicalFile(oldAttachment.getOriginalFilename());
                    attachmentRepository.delete(oldAttachment);
                }
                design.getImages().clear();
            }

            List<AttachmentDTO> attachmentDTOS = attachmentService.uploadMultiple(designCreateDTO.getImages());

            List<Attachment> newAttachments = attachmentDTOS.stream()
                    .map(a -> attachmentRepository.getByIdOrThrow(a.getId()))
                    .collect(Collectors.toList());

            design.getImages().addAll(newAttachments);
        }

        Design save = designRepository.save(design);
        return designMapper.toDTO(save);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Design design = designRepository.getByIdOrThrow(id);

        if (!design.getUser().getId().equals(user.getId()))
            throw new SecurityException(" : Bu dizayin sizga tegishli emas !!!");

        designRepository.delete(design);
    }

    @Override
    public List<DesignDTO> getAdmin() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Design> designs = designRepository.getByUser(user);

        return designMapper.toDTO(designs);

    }

    @Override
    public List<DesignDTO> search(String text) {

        List<Design> designs = designRepository.searchDesigns(text);

        return designMapper.toDTO(designs);
    }

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

    public void deletePhysicalFile(String filename) {
        try {
            Path path = Paths.get("C:\\Users\\HP ENVY\\IdeaProjects\\g50\\backend-link\\uploads").resolve(filename);
            Files.deleteIfExists(path);

        } catch (IOException e) {
            throw new RuntimeException("Fayil o'chirishda xatolik :" + e.getMessage());
        }

    }
}