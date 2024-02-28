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

import java.util.UUID;

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
       String tenantId= UUID.randomUUID().toString();
        tenantId = tenantId.toUpperCase().substring(0, 6);
        organizationDto.setOrgName(organizationDto.getOrgName());
        organizationDto.setOrgUsername(organizationDto.getOrgUsername());
        organizationDto.setOrgCode(tenantId);
        Organization organization = this.mapper.map(organizationDto, Organization.class);
        return organizationRepository.save(organization);
    }

    @Override
    public OrganizationDto getDefaultOrganization() {
        OrganizationDto orgDto= new OrganizationDto();
        orgDto.setId(1l);
        orgDto.setOrgUsername("BRAINQY");
        orgDto.setOrgName("BRAINQY Solutions");
        orgDto.setOrgCode("BRAINQY");
        return orgDto;
    }
}
