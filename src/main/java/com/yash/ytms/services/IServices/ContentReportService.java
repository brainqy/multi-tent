package com.yash.ytms.services.IServices;

import com.yash.ytms.dto.ContentReportDto;

import java.util.HashMap;
import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-03-2024
 */
public interface ContentReportService {
    public List<ContentReportDto> getAllReportContents();
    public ContentReportDto saveReportContent(ContentReportDto reportContent);
public List<HashMap<String, Object>> getContentReportReportedBy();
public HashMap getContentReportByLink(String link);
    public List<HashMap<String, Object>> getContentReportByGroupLink();

}
