package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.domain.ContentReport;
import com.brainqy.api.dto.ContentReportDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.repository.ContentReportRepository;
import com.brainqy.api.services.IServices.ContentReportService;
import com.brainqy.api.services.IServices.IYtmsUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class IContentReportServiceImplTest {

    @Mock
    private ContentReportRepository contentReportRepository;

    @Mock
    private IYtmsUserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private IContentReportServiceImpl contentReportService;

    private ContentReport contentReport;
    private ContentReportDto contentReportDto;
    private YtmsUserDto ytmsUserDto;

    @BeforeEach
    void setUp() {
        contentReport = new ContentReport();
        contentReport.setReportedBy("dvsomwanshi@gmail.com");
        contentReport.setLink("https://example.com");
        contentReport.setRating(4.5);
        contentReport.setReportedAt(LocalDateTime.now());

        contentReportDto = new ContentReportDto();
        contentReportDto.setReportedBy("dvsomwanshi@gmail.com");

        ytmsUserDto = new YtmsUserDto();
        ytmsUserDto.setEmailAdd("dvsomwanshi@gmail.com");
        ytmsUserDto.setFullName("John Doe");

        lenient().when(modelMapper.map(contentReport, ContentReportDto.class)).thenReturn(contentReportDto);
        lenient().when(modelMapper.map(contentReportDto, ContentReport.class)).thenReturn(contentReport);
    }

    @Test
    void testGetAllReportContents_NoReports() {
        lenient().when(contentReportRepository.findAll()).thenReturn(Collections.emptyList());
        List<ContentReportDto> reports = contentReportService.getAllReportContents();
        assertTrue(reports.isEmpty());
    }

    @Test
    void testGetAllReportContents_WithReports() {
        when(contentReportRepository.findAll()).thenReturn(List.of(contentReport));

        List<ContentReportDto> reports = contentReportService.getAllReportContents();

        assertFalse(reports.isEmpty());
        assertEquals(1, reports.size());
        verify(modelMapper, times(1)).map(contentReport, ContentReportDto.class);
    }

    @Test
    void testSaveReportContent() {
        when(contentReportRepository.save(any(ContentReport.class))).thenReturn(contentReport);
        when(userService.getUserByEmailAdd("dvsomwanshi@gmail.com")).thenReturn(ytmsUserDto);

        ContentReportDto savedReport = contentReportService.saveReportContent(contentReportDto);

        assertNotNull(savedReport);
        assertEquals("John Doe", savedReport.getReportedBy());
        verify(contentReportRepository, times(1)).save(any(ContentReport.class));
    }

    @Test
    void testGetContentReportReportedBy() {
        when(contentReportRepository.findAll()).thenReturn(List.of(contentReport));

        List<HashMap<String, Object>> result = contentReportService.getContentReportReportedBy();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("dvsomwanshi@gmail.com", result.get(0).get("reportedBy"));
        assertNotNull(result.get(0).get("reports"));
    }

    @Test
    void testGetContentReportByLink() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("dvsomwanshi@gmail.com");
        SecurityContextHolder.setContext(securityContext);

        when(contentReportRepository.getContentReportyLink("https://example.com")).thenReturn(List.of(contentReport));
        when(userService.getUserByEmailAdd("dvsomwanshi@gmail.com")).thenReturn(ytmsUserDto);

        HashMap<String, Object> result = contentReportService.getContentReportByLink("https://example.com");

        assertNotNull(result);
        assertNotNull(result.get("contentDto"));
        assertNotNull(result.get("avgRating"));
    }

    @Test
    void testCalculateAverageRating_NoReports() {
        double avgRating = contentReportService.calculateAverageRating(Collections.emptyList());
        assertEquals(0.0, avgRating);
    }

    @Test
    void testCalculateAverageRating_WithReports() {
        List<ContentReport> reports = List.of(
                contentReport,
                new ContentReport() {{
                    setRating(5.0);
                }}
        );

        double avgRating = contentReportService.calculateAverageRating(reports);
        assertEquals(4.75, avgRating);
    }
}
