package com.yash.ytms.controller;

import com.yash.ytms.domain.Organization;
import com.yash.ytms.dto.OrganizationDto;
import com.yash.ytms.services.IServices.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 14-02-2024
 */
@RestController
@RequestMapping("/org")
public class OrganizationController {

        @Autowired
        private IOrganizationService organizationService;

        @PostMapping("/create_org")
        public Organization createOrganization(@RequestBody OrganizationDto organization) {
            return organizationService.createOrganization(organization);
        }
}
