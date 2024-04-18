package com.yash.ytms.controller;

import com.yash.ytms.domain.atsscan.GenerateReportRequest;
import com.yash.ytms.domain.atsscan.SectionDataWrapperDto;
import com.yash.ytms.services.IServices.AtsScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    private AtsScanService scanService;
    @PostMapping("/generate-report")
    public ResponseEntity<SectionDataWrapperDto> generateReport(@RequestBody GenerateReportRequest request) {
        String resume = request.getResume();
        String jobDescription = request.getJobDescription();
        SectionDataWrapperDto datasamp = this.scanService.generateReports(resume, jobDescription);
        return new ResponseEntity<>(datasamp,HttpStatus.CREATED);
    }
    @GetMapping("/getLatestReport")

    public  ResponseEntity<SectionDataWrapperDto> getLatestReport(){
        SectionDataWrapperDto latestReport= this.scanService.getLatestReport();
       return  new ResponseEntity<>(latestReport,HttpStatus.OK);
    }
}
