package com.yash.ytms.controller;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 16-02-2024
 */

import com.yash.ytms.domain.Organization;
import com.yash.ytms.dto.OrganizationDto;
import com.yash.ytms.services.IServices.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/org")
public class CustomBotController {

    @Autowired
    private IOrganizationService organizationService;


    @PostMapping("/create_org")
    public ResponseEntity createOrg(@RequestBody OrganizationDto organizationDto){

        organizationService.createOrganization(organizationDto);
        return new ResponseEntity<>(organizationDto, HttpStatus.CREATED);
    }
}
