package com.brainqy.api.services.IServices;

import com.brainqy.api.domain.Organization;
import com.brainqy.api.dto.OrganizationDto;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 14-02-2024
 */
public interface IOrganizationService {
    public Organization createOrganization(OrganizationDto organizationDto);
    public OrganizationDto findOrganizationByOrgCode(String orgCode);
}
