package com.yash.ytms;

import com.yash.ytms.domain.Organization;
import com.yash.ytms.dto.OrganizationDto;
import com.yash.ytms.repository.OrganizationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YtmsApplication  implements CommandLineRunner {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private OrganizationRepository organizationRepository;
    public static void main(String[] args) {
        SpringApplication.run(YtmsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //you have to add one organization at the start of first run
/*        OrganizationDto orgDto= new OrganizationDto();
        orgDto.setOrgUsername("BRAINQY");
        orgDto.setOrgName("BRAINQY Solutions");
        orgDto.setOrgCode("BRAINQY");
        Organization organization = this.mapper.map(orgDto, Organization.class);
        organizationRepository.save(organization);*/
    }
}
