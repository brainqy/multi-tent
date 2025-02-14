package com.brainqy.api.controller;

import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.domain.atsscan.GenerateReportRequest;
import com.brainqy.api.domain.atsscan.SectionDataWrapperDto;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 16-04-2024
 */
@RestController
public class AIATSController {
    private final ChatClient chatClient;

    public AIATSController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/topAthlete")
    public String topAthlete(@RequestParam("subject") String subject) {
        return chatClient.call("give me the list of "+subject+" in the world in json format with their income, family, sports team");
    }
    @PostMapping("/prompt")
        public String getReport(@RequestBody GenerateReportRequest request){
        String resume = request.getResume();
        String jobDescription = request.getJobDescription();
        return chatClient.call("generate a json  report by comparing "+resume+" and "+jobDescription+" in json format with allData:[{section:Searchability,\n" +
                " percentage:calculate % based on weight and true tickIcons,issues:count number of false tickIcon,\n" +
                " data:[{title:Contact info,infoIcon:true,content:[{tickIcon:true if phone number is found,contentValue:check phone number.},{tickIcon:true if email is found,contentValue:Reason why behind the tikIcon status.},{tickIcon:true if address is found,contentValue:Reason why behind the tikIcon status.}],weight:5},\n" +
                " {section:Recruiter Tips,\n" +
                " percentage:calculate % based on weight and true tickIcons,issues:count number of false tickIcon,\n" +
                " data:[{title:Word count ,infoIcon:true,content:[{tickIcon:true if word count > 600 words,contentValue: Reason why behind the tikIcon status.},{tickIcon:true if any specific achievment found ,contentValue:Reason why behind the tikIcon status ,mention  meaurable results (e.g. time saved, increase in sales, etc)..},{tickIcon:true if job matches seniority level,contentValue:Reason why behind the tikIcon status, mention seniority level .provide sugestion if not matches.},{tickIcon:true if negative words are not found,contentValue:mention negative words in resume if found .}],weight:5},\n" +
                "],finalProgress:as final % aggregate progress");

}


    @PostMapping("/generateGpt")
    public SectionDataWrapperDto generateReport(@RequestBody GenerateReportRequest request) {
        var outputParser = new BeanOutputParser<>(SectionDataWrapperDto.class);
        String resume = request.getResume();
        String jobDescription = request.getJobDescription();

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
        return reportDto;
    }


    private String generateSections() {
        return """
        - Section: "Searchability"
          Percentage: 0.0
          Issues: 5
          Data:
            - Title: "Contact info"
              InfoIcon: true
              Content:
                - TickIcon: false
                  ContentValue: {section1content1Value1}
                - TickIcon: false
                  ContentValue: {section1content1Value2}
              Weight: 0.0
            - Title: "Job title match"
              InfoIcon: true
              Content:
                - TickIcon: false
                  ContentValue: {section1content2Value1}
              Weight: 0.0
            - Title: "Section headings"
              InfoIcon: true
              Content:
                - TickIcon: false
                  ContentValue: {section1content3Value1}
                - TickIcon: false
                  ContentValue: {section1content3Value2}
              Weight: 0.0
        """;
    }


}
