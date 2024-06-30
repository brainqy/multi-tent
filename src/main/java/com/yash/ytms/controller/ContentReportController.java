package com.yash.ytms.controller;

import com.yash.ytms.domain.ContentReport;
import com.yash.ytms.dto.ContentReportDto;
import com.yash.ytms.dto.ContentReportedByLinkDto;
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.services.IServices.ContentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RestController
@RequestMapping("/content-reports")
public class ContentReportController {
    @Autowired
    private ContentReportService contentReportService;

    @PostMapping("/save")
    public ResponseEntity<ContentReportDto> saveReportContent(@RequestBody ContentReportDto contentReportDto) {
        ContentReportDto savedReport = contentReportService.saveReportContent(contentReportDto);
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }
    @GetMapping("/get-all")
    public ResponseEntity<ResponseWrapperDto> getAllContentReport() {
        List<ContentReportDto> allContentReports = contentReportService.getAllReportContents();
        ResponseWrapperDto wrapperDto = new ResponseWrapperDto();

        if (allContentReports.isEmpty()) {
            wrapperDto.setStatus("FAIL");
            wrapperDto.setMessage("No content reports found!");
            wrapperDto.setData(List.of()); // Return an empty list as data
            return new ResponseEntity<>(wrapperDto, HttpStatus.OK);
        }

        wrapperDto.setStatus("SUCCESS");
        wrapperDto.setMessage("Content reports found");
        wrapperDto.setData(allContentReports);

        return new ResponseEntity<>(wrapperDto, HttpStatus.OK);
    }
    @GetMapping("/getForLink")
    public ResponseEntity getContentReportbyLink(@RequestParam String link) {
        HashMap content = contentReportService.getContentReportByLink(link);
        return new ResponseEntity(content, HttpStatus.OK);
    }
    @GetMapping("/getFbyGroupLink")
    public ResponseEntity getContentReportByGroupLink() {
        List<HashMap<String, Object>> content = contentReportService.getContentReportByGroupLink();
        return new ResponseEntity(content, HttpStatus.OK);
    }
    @GetMapping("/getContentReportedBy")
    ResponseEntity getContentReportReportedBy(){
        List<HashMap<String, Object>> content = contentReportService.getContentReportReportedBy();
        return new ResponseEntity(content, HttpStatus.OK);
    }
    


}
