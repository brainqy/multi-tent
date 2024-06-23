package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.atsscan.SectionDataWrapperDto;

import java.security.Principal;
import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 15-04-2024
 */
public interface AtsScanService {
    public SectionDataWrapperDto generateReports(String resume, String jobDescription, Principal principal);

    public  SectionDataWrapperDto getLatestReport(Principal principal);

    List<SectionDataWrapperDto> getScanHistoryByUser(Principal principal);
}
