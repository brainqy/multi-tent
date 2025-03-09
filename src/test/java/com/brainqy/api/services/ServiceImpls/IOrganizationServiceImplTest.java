package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 10-03-2025
 */
import com.brainqy.api.domain.Organization;
import com.brainqy.api.dto.OrganizationDto;
import com.brainqy.api.repository.OrganizationRepository;
import com.brainqy.api.repository.YtmsUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IOrganizationServiceImplTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private YtmsUserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private IOrganizationServiceImpl organizationService;

    private OrganizationDto organizationDto;
    private Organization organization;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        organizationDto = new OrganizationDto();
        organizationDto.setOrgName("Test Org");
        organizationDto.setOrgUsername("testorg");
        organizationDto.setOrgCode("ABC123");

        organization = new Organization();
        organization.setOrgName("Test Org");
        organization.setOrgUsername("testorg");
        organization.setOrgCode("ABC123");
    }

    @Test
    void createOrganization_ShouldCreateOrganization() {
        when(mapper.map(any(OrganizationDto.class), eq(Organization.class))).thenReturn(organization);
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        Organization result = organizationService.createOrganization(organizationDto);

        assertNotNull(result);
        assertEquals("Test Org", result.getOrgName());
        assertEquals("testorg", result.getOrgUsername());
        assertEquals("ABC123", result.getOrgCode());
        verify(organizationRepository, times(1)).save(organization);
    }

    @Test
    void findOrganizationByOrgCode_ShouldReturnOrganizationDto() {
        when(organizationRepository.findOrganizationByOrgCode(anyString())).thenReturn(organization);
        when(mapper.map(any(Organization.class), eq(OrganizationDto.class))).thenReturn(organizationDto);

        OrganizationDto result = organizationService.findOrganizationByOrgCode("ABC123");

        assertNotNull(result);
        assertEquals("Test Org", result.getOrgName());
        assertEquals("testorg", result.getOrgUsername());
        assertEquals("ABC123", result.getOrgCode());
        verify(organizationRepository, times(1)).findOrganizationByOrgCode("ABC123");
    }
}