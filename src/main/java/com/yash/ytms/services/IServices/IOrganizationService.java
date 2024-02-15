package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.Organization;
import com.yash.ytms.dto.OrganizationDto;

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
}
