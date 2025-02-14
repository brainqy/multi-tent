package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.domain.Organization;
import com.brainqy.api.dto.OrganizationDto;
import com.brainqy.api.repository.OrganizationRepository;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.services.IServices.IOrganizationService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(IOrganizationServiceImpl.class);

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
    public OrganizationDto findOrganizationByOrgCode(String orgCode) {
        Organization org = organizationRepository.findOrganizationByOrgCode(orgCode);
        OrganizationDto orgDto = mapper.map(org, OrganizationDto.class);
        return orgDto;
    }
}
