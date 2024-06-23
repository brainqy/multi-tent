package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.atsscan.SectionDataWrapper;
import com.yash.ytms.domain.atsscan.SectionDataWrapperDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.AtsRepository;
import com.yash.ytms.services.IServices.AtsScanService;
import org.modelmapper.ModelMapper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 03-05-2024
 */
@Service
@Qualifier("gptScan")
public class AtsScanGptServiceImpl implements AtsScanService {
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AtsRepository atsRepository;
    @Override
    public SectionDataWrapperDto generateReports(String resume, String jobDescription, Principal principal) {
        var outputParser = new BeanOutputParser<>(SectionDataWrapperDto.class);


        String userMessage =
                """
                        Generate a comparison report in number of sections:
                                        
                        - Section: "Reachability Report"
                          Percentage: {comparisonPercentage},
                          Issues: {comparisonIssues}
                          Data:
                          
                            - Title: "Contact Information"
                              InfoIcon: false
                              Content:
                                - TickIcon: {contactNumberTickIcon}
                                  ContentValue(e.g. Contact number is available): "{contactNumberContentValue}"
                                - TickIcon: {emailTickIcon}
                                  ContentValue(e.g email is found): "{emailContentValue}"
                              Weight: 1.0
                        - Section:" Job insights"    
                            - Title: "Job Match"
                              InfoIcon: false
                              Content:
                                - TickIcon: {jobTitleTickIcon}
                                  ContentValue(write if job title matches or not. e.g. Job Title matches Job description): "{jobTitleContentValue}"
                                - TickIcon: {workExperienceTickIcon}
                                  ContentValue(write if work experience matches or not. e.g. Work experience matches given Job description): "{workExperienceContentValue}"
                                - TickIcon: {educationalSectionTickIcon}
                                  ContentValue(write if education section found or not ): "{educationalSectionContentValue}"
                              Weight: 2.0
                         - Section: " Skills"   
                            - Title: "Skills and Achievements"
                              InfoIcon: false
                              Content:
                                - TickIcon: {hardSkillsTickIcon}
                                  ContentValue(what are the skills  found in resume. e.g. java, python): "{hardSkillsContentValue}"
                                - TickIcon: {idealWordCountTickIcon}
                                  ContentValue(write about resume matches ideal word count which is 1000 words): "{idealWordCountContentValue}"
                                - TickIcon: {experienceSeniorityTickIcon}
                                  ContentValue( write about experience matches with job level): "{experienceSeniorityContentValue}"
                                - TickIcon: {measurableAchievementsTickIcon}
                                  ContentValue(write if any  measurable achievements found in the resume. e.g. awards): "{measurableAchievementsContentValue}"
                                - TickIcon: {webPresenceLinksTickIcon}
                                  ContentValue(write if web presence is available or not): "{webPresenceLinksContentValue}"
                              Weight: 3.0
                          
                          {format}
                        """;


        // Populate the variables map with template variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("contactNumberContentValue", ""); // Replace with actual content value for contact number
        variables.put("emailContentValue", ""); // Replace with actual content value for email
        variables.put("jobTitleContentValue", ""); // Replace with actual content value for job title
        variables.put("workExperienceContentValue", ""); // Replace with actual content value for work experience
        variables.put("educationalSectionContentValue", ""); // Replace with actual content value for educational section
        variables.put("hardSkillsContentValue", ""); // Replace with actual content value for hard skills
        variables.put("idealWordCountContentValue", ""); // Replace with actual content value for ideal word count
        variables.put("experienceSeniorityContentValue", ""); // Replace with actual content value for experience seniority
        variables.put("measurableAchievementsContentValue", ""); // Replace with actual content value for measurable achievements
        variables.put("webPresenceLinksContentValue", ""); // Replace with actual content value for web presence links

        variables.put("contactNumberTickIcon", ""); // Replace with actual boolean value for contact number tick icon
        variables.put("emailTickIcon", ""); // Replace with actual boolean value for email tick icon
        variables.put("jobTitleTickIcon", ""); // Replace with actual boolean value for job title tick icon
        variables.put("workExperienceTickIcon", ""); // Replace with actual boolean value for work experience tick icon
        variables.put("educationalSectionTickIcon", ""); // Replace with actual boolean value for educational section tick icon
        variables.put("hardSkillsTickIcon", ""); // Replace with actual boolean value for hard skills tick icon
        variables.put("idealWordCountTickIcon", ""); // Replace with actual boolean value for ideal word count tick icon
        variables.put("experienceSeniorityTickIcon", ""); // Replace with actual boolean value for experience seniority tick icon
        variables.put("measurableAchievementsTickIcon", ""); // Replace with actual boolean value for measurable achievements tick icon
        variables.put("webPresenceLinksTickIcon", ""); // Replace with actual boolean value for web presence links tick icon

        variables.put("comparisonPercentage", ""); // Replace with actual comparison percentage
        variables.put("comparisonIssues", ""); // Replace with actual comparison issues
        variables.put("format", outputParser.getFormat());


        // Create the prompt template and call the API
        PromptTemplate promptTemplate = new PromptTemplate(userMessage, variables);
        Prompt prompt = promptTemplate.create();
        SectionDataWrapperDto reportDto;
        try {
            Generation generation = chatClient.call(prompt).getResult();
            // Parse the response and return the report
            reportDto = outputParser.parse(generation.getOutput().getContent());
        } catch (Exception e) {
            throw new ApplicationException("Error while calling ai api");
        }
        SectionDataWrapper sectionDataWrapper = modelMapper.map(reportDto,SectionDataWrapper.class);
        sectionDataWrapper.setCreatedAt(LocalDateTime.now());
        sectionDataWrapper.setCreatedBy(principal.getName());
        atsRepository.save(sectionDataWrapper);
        return reportDto;
    }

    @Override
    public SectionDataWrapperDto getLatestReport(Principal principal) {
        SectionDataWrapper latestReport= atsRepository.findFirstByOrderByCreatedAtDesc();
        latestReport.setCreatedBy(principal.getName());
        return modelMapper.map(latestReport,SectionDataWrapperDto.class);

    }

    @Override
    public List<SectionDataWrapperDto> getScanHistoryByUser(Principal principal) {
        String userName= principal.getName();
        List<SectionDataWrapper> scanHistory = atsRepository.findByUser(userName);
        List<SectionDataWrapperDto> scanHistoryDto = scanHistory.stream()
                .map(wrapper -> modelMapper.map(wrapper, SectionDataWrapperDto.class))
                .collect(Collectors.toList());
        return scanHistoryDto;
    }
}
