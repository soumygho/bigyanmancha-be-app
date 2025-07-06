package com.vigyanmancha.backend.utility;

import com.vigyanmancha.backend.domain.postgres.VigyanKendraDetails;
import com.vigyanmancha.backend.dto.request.VigyanKendraDetailsRequestDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VigyanKendraDetailsMapper {
    public VigyanKendraDetailsRequestDTO toDTO(VigyanKendraDetails entity) {
        VigyanKendraDetailsRequestDTO dto = new VigyanKendraDetailsRequestDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        return dto;
    }
}
