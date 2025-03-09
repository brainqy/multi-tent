package com.brainqy.api.util;

import com.brainqy.api.domain.Organization;
import com.brainqy.api.dto.OrganizationDto;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */
// OrganizationConverter.java
import com.brainqy.api.domain.Organization;
import com.brainqy.api.dto.OrganizationDto;
import org.springframework.stereotype.Component;

@Component

public class OrganizationConverter {

    public static Organization convertToEntity(OrganizationDto dto) {
        if (dto == null) {
            return null;
        }

        Organization organization = new Organization();
        organization.setId(dto.getId());
        organization.setOrgName(dto.getOrgName());
        organization.setOrgCode(dto.getOrgCode());
        organization.setOrgUsername(dto.getOrgUsername());
        return organization;
    }

    public static OrganizationDto convertToDto(Organization entity) {
        if (entity == null) {
            return null;
        }

        OrganizationDto dto = new OrganizationDto();
        dto.setId(entity.getId());
        dto.setOrgName(entity.getOrgName());
        dto.setOrgCode(entity.getOrgCode());
        dto.setOrgUsername(entity.getOrgUsername());
        return dto;
    }
}