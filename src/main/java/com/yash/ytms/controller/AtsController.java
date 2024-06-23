package com.yash.ytms.controller;

import com.yash.ytms.domain.atsscan.GenerateReportRequest;
import com.yash.ytms.domain.atsscan.SectionDataWrapperDto;
import com.yash.ytms.services.IServices.AtsScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class AtsController {
    @Autowired
    @Qualifier("gptScan")
    private AtsScanService scanService;
    @PostMapping("/generate-report")
    public ResponseEntity<SectionDataWrapperDto> generateReport(@RequestBody GenerateReportRequest request, Principal principal) {
        String resume = request.getResume();
        String jobDescription = request.getJobDescription();
        SectionDataWrapperDto datasamp = this.scanService.generateReports(resume, jobDescription,principal);
        return new ResponseEntity<>(datasamp,HttpStatus.CREATED);
    }
    @GetMapping("/getLatestReport")

    public  ResponseEntity<SectionDataWrapperDto> getLatestReport(Principal principal){
        SectionDataWrapperDto latestReport= this.scanService.getLatestReport(principal);
       return  new ResponseEntity<>(latestReport,HttpStatus.OK);
    }
    @GetMapping("/get-scan-history")

    public ResponseEntity getListOfReportsyUser(Principal principal){
        List<SectionDataWrapperDto> scanHistory= this.scanService.getScanHistoryByUser(principal);
        return  new ResponseEntity<>(scanHistory,HttpStatus.OK);
    }
}
