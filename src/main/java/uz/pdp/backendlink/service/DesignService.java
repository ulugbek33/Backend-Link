package uz.pdp.backendlink.service;

import jakarta.validation.Valid;
import uz.pdp.backendlink.dto.DesignCreateDTO;
import uz.pdp.backendlink.dto.DesignDTO;
import uz.pdp.backendlink.dto.PageableDTO;

import java.util.List;

public interface DesignService {

    DesignDTO getDesignById(Long id);

    void saveDesign(DesignCreateDTO designCreateDTO);

    PageableDTO getAll(int page, int size);

    DesignDTO edit(Long id, DesignCreateDTO designCreateDTO);

    void delete(Long id);
}
