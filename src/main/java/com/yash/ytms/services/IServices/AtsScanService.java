package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.atsscan.SectionDataWrapperDto;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 15-04-2024
 */
public interface AtsScanService {
    public SectionDataWrapperDto generateReports(String resume, String jobDescription);

    public  SectionDataWrapperDto getLatestReport();
}
