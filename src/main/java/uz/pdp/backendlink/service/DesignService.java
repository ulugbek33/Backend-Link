package uz.pdp.backendlink.service;

import uz.pdp.backendlink.dto.DesignCreateDTO;
import uz.pdp.backendlink.dto.DesignDTO;

public interface DesignService {

    DesignDTO getDesignById(Long id);

    void saveDesign(DesignCreateDTO designCreateDTO);
}
