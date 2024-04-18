package com.yash.ytms.controller;

import com.yash.ytms.domain.atsscan.GenerateReportRequest;
import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.*;

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
}
