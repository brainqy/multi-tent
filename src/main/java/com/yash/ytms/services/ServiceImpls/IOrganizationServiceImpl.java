package com.yash.ytms.services.ServiceImpls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.ytms.domain.Organization;
import com.yash.ytms.dto.OrganizationDto;
import com.yash.ytms.repository.OrganizationRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.IOrganizationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 14-02-2024
 */
@Service
public class IOrganizationServiceImpl implements IOrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private YtmsUserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public Organization createOrganization(OrganizationDto organizationDto) {
        organizationDto.setOrgName(organizationDto.getOrgName());
        organizationDto.setOrgUsername(organizationDto.getOrgUsername());
        Organization organization = this.mapper.map(organizationDto, Organization.class);
        return organizationRepository.save(organization);
    }
}
