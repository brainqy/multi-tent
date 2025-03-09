package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 10-03-2025
 */
import com.brainqy.api.domain.atsscan.*;
import com.brainqy.api.repository.AtsRepository;
import com.brainqy.api.services.ServiceImpls.AtsScanServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtsScanServiceImplTest {

    @Mock
    private AtsRepository atsRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AtsScanServiceImpl atsScanService;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateReports_ShouldGenerateReportSuccessfully() {
        String resume = "Sample resume content";
        String jobDescription = "Sample job description";
        String userName = "testUser";

        when(principal.getName()).thenReturn(userName);

        SectionDataWrapperDto result = atsScanService.generateReports(resume, jobDescription, principal);

        assertNotNull(result);
        assertEquals(6, result.getAllData().size());
        assertEquals(userName, result.getCreatedBy());
        verify(atsRepository, times(1)).save(any(SectionDataWrapper.class));
    }

    @Test
    void getLatestReport_ShouldReturnLatestReport() {
        String userName = "testUser";
        when(principal.getName()).thenReturn(userName);

        SectionDataWrapper latestReport = new SectionDataWrapper();
        when(atsRepository.findFirstByOrderByCreatedAtDesc()).thenReturn(latestReport);
        when(modelMapper.map(any(SectionDataWrapper.class), eq(SectionDataWrapperDto.class)))
                .thenReturn(new SectionDataWrapperDto());

        SectionDataWrapperDto result = atsScanService.getLatestReport(principal);

        assertNotNull(result);
        verify(atsRepository, times(1)).findFirstByOrderByCreatedAtDesc();
    }

    @Test
    void getLatestReport_ShouldReturnDefaultDtoWhenNoReportFound() {
        when(atsRepository.findFirstByOrderByCreatedAtDesc()).thenReturn(null);

        SectionDataWrapperDto result = atsScanService.getLatestReport(principal);

        assertNotNull(result);
        assertTrue(result.getAllData().isEmpty());
    }

    @Test
    void getScanHistoryByUser_ShouldReturnScanHistory() {
        String userName = "testUser";
        when(principal.getName()).thenReturn(userName);

        List<SectionDataWrapper> scanHistory = Collections.singletonList(new SectionDataWrapper());
        when(atsRepository.findByUser(userName)).thenReturn(scanHistory);
        when(modelMapper.map(any(SectionDataWrapper.class), eq(SectionDataWrapperDto.class)))
                .thenReturn(new SectionDataWrapperDto());

        List<SectionDataWrapperDto> result = atsScanService.getScanHistoryByUser(principal);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(atsRepository, times(1)).findByUser(userName);
    }

    @Test
    void saveAsStarred_ShouldToggleStarredStatus() {
        Long id = 1L;
        SectionDataWrapper entity = new SectionDataWrapper();
        entity.setStarred(false);

        when(atsRepository.findById(id)).thenReturn(Optional.of(entity));
        when(modelMapper.map(any(SectionDataWrapper.class), eq(SectionDataWrapperDto.class)))
                .thenReturn(new SectionDataWrapperDto());

        SectionDataWrapperDto result = atsScanService.saveAsStarred(id);

        assertNotNull(result);
        assertTrue(entity.isStarred());
        verify(atsRepository, times(1)).save(entity);
    }

    @Test
    void saveAsStarred_ShouldThrowExceptionWhenEntityNotFound() {
        Long id = 1L;
        when(atsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> atsScanService.saveAsStarred(id));
    }
}