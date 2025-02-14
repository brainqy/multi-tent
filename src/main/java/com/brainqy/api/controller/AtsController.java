package com.brainqy.api.controller;

import com.brainqy.api.domain.atsscan.GenerateReportRequest;
import com.brainqy.api.domain.atsscan.SectionDataWrapperDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.services.IServices.AtsScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Qualifier("noGptScan")
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
    @PutMapping("/{id}/star")
    public ResponseEntity<SectionDataWrapperDto> saveAsStarred(@PathVariable Long id) {
        SectionDataWrapperDto updatedEntity = this.scanService.saveAsStarred(id);
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        wrapperDto.setStatus("SUCCESS");
        wrapperDto.setData(updatedEntity);
        return new ResponseEntity(wrapperDto,HttpStatus.OK);
    }
}
