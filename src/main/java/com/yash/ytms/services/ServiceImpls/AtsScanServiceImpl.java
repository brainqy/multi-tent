package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.atsscan.*;
import com.yash.ytms.repository.AtsRepository;
import com.yash.ytms.services.IServices.AtsScanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 15-04-2024
 */
@Service
public class AtsScanServiceImpl implements AtsScanService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AtsRepository atsRepository;
    public SectionDataWrapperDto generateReports(String resume, String jobDescription, Principal principal){
double searchabilityWeight=0.1;
double recruiterTipsWeihgt=0.2;
double formattingWeight=0.1;
double highlightWeight=0.2;
double hardSkillWeight=0.3;
double softSkillWeight=0.1;
        SectionDataWrapperDto wrapper= new SectionDataWrapperDto();
List<SectionDataDto> data= new ArrayList<>();
        SectionDataDto sr = generateSerchabilityReport(resume, jobDescription);
        SectionDataDto rt = generateRecruiterTipsReport(resume, jobDescription);
        SectionDataDto fr=generateFormattingReport(resume ,jobDescription);
        SectionDataDto hr=generateHighlightsReport(resume,jobDescription);
        SectionDataDto hs=generateHardSkillsReport(resume,jobDescription);
        SectionDataDto ss=generateSoftSkillsReport(resume,jobDescription);
        double finalProgress = 10*(sr.getPercentage()*searchabilityWeight + rt.getPercentage()*recruiterTipsWeihgt + fr.getPercentage()*formattingWeight + hr.getPercentage()*highlightWeight
                + hr.getPercentage()*highlightWeight
                + hs.getPercentage()*hardSkillWeight  + ss.getPercentage()*softSkillWeight)/6;

        data.add(sr);
        data.add(rt);
        data.add(fr);
        data.add(hr);
        data.add(hs);
        data.add(ss);
        wrapper.setAllData(data);
        wrapper.setFinalProgress(finalProgress);
        LocalDateTime localDateTime= LocalDateTime.now();
        wrapper.setCreatedAt(localDateTime.toString());
        SectionDataWrapper wrapperEntity = modelMapper.map(wrapper, SectionDataWrapper.class);
        atsRepository.save(wrapperEntity);
        return wrapper;
    }

    @Override
    public SectionDataWrapperDto getLatestReport(Principal principal) {
        SectionDataWrapper latestReport= atsRepository.findFirstByOrderByCreatedAtDesc();
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

    public SectionDataDto generateSerchabilityReport(String resume, String jobDescription) {
        List<DataItemDto> dataItems = new ArrayList<>();

        // Define weights for each section
        double contactInfoWeight = 0.2;
        double jobTitleMatchWeight = 0.3;
        double educationMatchWeight = 0.2;
        double sectionHeadingsWeight = 0.1;
        double dateFormattingWeight = 0.2;

        // Compare contact info
        DataItemDto contactInfo = new DataItemDto();
        contactInfo.setTitle("Contact info");
        contactInfo.setInfoIcon(true);
        List<ContentItemDto> contactContent = new ArrayList<>();
        // Compare phone number
        if (resume.contains("phone")) {
            ContentItemDto phoneContent = new ContentItemDto();
            phoneContent.setTickIcon(true);
            phoneContent.setContentValue("You provided your phone number.");
            contactContent.add(phoneContent);
        } else {
            ContentItemDto phoneContent = new ContentItemDto();
            phoneContent.setTickIcon(false);
            phoneContent.setContentValue("You did not provide your phone number.");
            contactContent.add(phoneContent);
        }
        // Compare email
        if (resume.contains("email")) {
            ContentItemDto emailContent = new ContentItemDto();
            emailContent.setTickIcon(true);
            emailContent.setContentValue("You provided your email.");
            contactContent.add(emailContent);
        } else {
            ContentItemDto emailContent = new ContentItemDto();
            emailContent.setTickIcon(false);
            emailContent.setContentValue("You did not provide your email.");
            contactContent.add(emailContent);
        }
        // Add contact content to data item
        contactInfo.setContent(contactContent);
        // Calculate weighted score for contact info
        double contactInfoScore = calculateScore(contactContent);
        contactInfo.setWeight(contactInfoWeight * contactInfoScore);
        // Add weighted contact info to data items list
        dataItems.add(contactInfo);
        int contactInfoIssues = countIssues(contactContent);
        // Similarly, you can compare other sections like job title match, education match, etc.
        DataItemDto jobTitleMatch = new DataItemDto();
        jobTitleMatch.setTitle("Job title match");
        jobTitleMatch.setInfoIcon(true);
        List<ContentItemDto> jobTitleMatchContent = new ArrayList<>();
        // Compare job title
        if (resume.contains("Full Stack Developer")) {
            ContentItemDto jobTitleContent = new ContentItemDto();
            jobTitleContent.setTickIcon(true);
            jobTitleContent.setContentValue("The Full Stack Developer job title provided or found in the job description was found in your resume.");
            jobTitleMatchContent.add(jobTitleContent);
        } else {
            ContentItemDto jobTitleContent = new ContentItemDto();
            jobTitleContent.setTickIcon(false);
            jobTitleContent.setContentValue("The Full Stack Developer job title provided or found in the job description was not found in your resume.");
            jobTitleMatchContent.add(jobTitleContent);
        }
        // Add job title match content to data item
        jobTitleMatch.setContent(jobTitleMatchContent);
        // Calculate weighted score for job title match
        double jobTitleMatchScore = calculateScore(jobTitleMatchContent);
        jobTitleMatch.setWeight(jobTitleMatchWeight * jobTitleMatchScore);
        // Add weighted job title match to data items list
        dataItems.add(jobTitleMatch);
        int jobTitleMatchIssues = countIssues(jobTitleMatchContent);

        DataItemDto sectionHeadings = new DataItemDto();
        sectionHeadings.setTitle("Section headings");
        sectionHeadings.setInfoIcon(true);
        List<ContentItemDto> sectionHeadingsContent = new ArrayList<>();
        // Check for "Work Experience" section
        if (resume.contains("Work Experience")) {
            ContentItemDto workExpContent = new ContentItemDto();
            workExpContent.setTickIcon(true);
            workExpContent.setContentValue("We found the work experience section in your resume.");
            sectionHeadingsContent.add(workExpContent);
        } else {
            ContentItemDto workExpContent = new ContentItemDto();
            workExpContent.setTickIcon(false);
            workExpContent.setContentValue("We did not find the work experience section in your resume.");
            sectionHeadingsContent.add(workExpContent);
        }
        // Check for "Education" section
        if (resume.contains("Education")) {
            ContentItemDto eduContent = new ContentItemDto();
            eduContent.setTickIcon(true);
            eduContent.setContentValue("We found the education section in your resume.");
            sectionHeadingsContent.add(eduContent);
        } else {
            ContentItemDto eduContent = new ContentItemDto();
            eduContent.setTickIcon(false);
            eduContent.setContentValue("We did not find the education section in your resume.");
            sectionHeadingsContent.add(eduContent);
        }
        // Add section headings content to data item
        sectionHeadings.setContent(sectionHeadingsContent);
        // Calculate weighted score for section headings
        double sectionHeadingsScore = calculateScore(sectionHeadingsContent);
        sectionHeadings.setWeight(sectionHeadingsWeight * sectionHeadingsScore);
        // Add weighted section headings to data items list
        dataItems.add(sectionHeadings);

        // Calculate total issues for section headings
        int sectionHeadingsIssues = countIssues(sectionHeadingsContent);


        // Calculate total weight
        double totalWeight = contactInfoWeight + jobTitleMatchWeight + educationMatchWeight + sectionHeadingsWeight + dateFormattingWeight;

        // Calculate overall percentage
        double overallPercentage = ((contactInfoWeight * contactInfoScore) + (jobTitleMatchWeight * jobTitleMatchScore))+(sectionHeadingsWeight*sectionHeadingsScore) / totalWeight * 100;
        int totalIssues = contactInfoIssues+jobTitleMatchIssues +sectionHeadingsIssues;
        // Create SectionData object
        SectionDataDto sectionData = new SectionDataDto();
        sectionData.setSection("Searchability");
        sectionData.setPercentage(overallPercentage);
        sectionData.setIssues(totalIssues);
        sectionData.setData(dataItems);

        return sectionData;
    }
    public SectionDataDto generateRecruiterTipsReport(String resume, String jobDescription) {
        List<DataItemDto> dataItems = new ArrayList<>();

        // Define weights for each section
        double wordCountWeight = 0.2;
        double measurableResultsWeight = 0.2;
        double jobLevelMatchWeight = 0.3;
        double wordsToAvoidWeight = 0.3;

        // Compare word count
        // Implementation for word count comparison...
        DataItemDto wordCount = new DataItemDto();
        wordCount.setTitle("Word count");
        wordCount.setInfoIcon(true);
        List<ContentItemDto> wordCountContent = new ArrayList<>();

        int resumeWordCount = resume.split("\\s+").length;
        ContentItemDto wordCountItem = new ContentItemDto();
        wordCountItem.setTickIcon(true);
        wordCountItem.setContentValue("There are " + resumeWordCount + " words in your resume, which is under the suggested 1000 word count for relevance and ease of reading reasons.");
        wordCountContent.add(wordCountItem);

        // Add word count content to data item
        wordCount.setContent(wordCountContent);

        // Calculate weighted score for word count
        double wordCountScore = calculateScore(wordCountContent);
        wordCount.setWeight(wordCountWeight * wordCountScore);

        // Add weighted word count to data items list
        dataItems.add(wordCount);

        // Calculate total issues for word count
        int wordCountIssues = countIssues(wordCountContent);

        // Compare measurable result
        // Implementation for measurable results comparison...
        DataItemDto measurableResults = new DataItemDto();
        measurableResults.setTitle("Measurable results");
        measurableResults.setInfoIcon(true);
        List<ContentItemDto> measurableResultsContent = new ArrayList<>();

        // Count mentions of measurable results in resume
        int measurableResultsCount = countMeasurableResults(resume);

        ContentItemDto measurableResultsItem = new ContentItemDto();
        measurableResultsItem.setTickIcon(measurableResultsCount >= 5);
        if (measurableResultsCount >= 5) {
            measurableResultsItem.setContentValue("We found " + measurableResultsCount + " mentions of measurable results in your resume. You have met the requirement.");
        } else {
            measurableResultsItem.setContentValue("We found " + measurableResultsCount + " mentions of measurable results in your resume. Consider adding at least 5 specific achievements or impact you had in your job (e.g. time saved, increase in sales, etc).");
        }
        measurableResultsContent.add(measurableResultsItem);

        // Add measurable results content to data item
        measurableResults.setContent(measurableResultsContent);

        // Calculate weighted score for measurable results
        double measurableResultsScore = calculateScore(measurableResultsContent);
        measurableResults.setWeight(measurableResultsWeight * measurableResultsScore);

        // Add weighted measurable results to data items list
        dataItems.add(measurableResults);

        // Calculate total issues for measurable results
        int measurableResultsIssues = countIssues(measurableResultsContent);



        // Compare job level match
        // Implementation for job level match comparison...
        DataItemDto jobLevelMatch = new DataItemDto();
        jobLevelMatch.setTitle("Job level match");
        jobLevelMatch.setInfoIcon(true);
        List<ContentItemDto> jobLevelMatchContent = new ArrayList<>();

        // Check for seniority level match
        boolean seniorityMatch = checkSeniorityMatch(resume, jobDescription);
        ContentItemDto seniorityContent = new ContentItemDto();
        seniorityContent.setTickIcon(seniorityMatch);
        if (seniorityMatch) {
            seniorityContent.setContentValue("Your previous experience matches the seniority level of the job you are applying for.");
        } else {
            seniorityContent.setContentValue("You are applying to a junior-level role, but you held senior or higher level positions in the past. If you are a career changer in a new field, we recommend adding a summary statement to explain your shift in trajectory.");
        }
        jobLevelMatchContent.add(seniorityContent);

        // Add job level match content to data item
        jobLevelMatch.setContent(jobLevelMatchContent);

        // Calculate weighted score for job level match
        double jobLevelMatchScore = calculateScore(jobLevelMatchContent);
        jobLevelMatch.setWeight(jobLevelMatchWeight * jobLevelMatchScore);

        // Add weighted job level match to data items list
        dataItems.add(jobLevelMatch);

        // Calculate total issues for job level match
        int jobLevelMatchIssues = countIssues(jobLevelMatchContent);


        // Compare words to avoid

        // Implementation for words to avoid comparison...
        DataItemDto wordsToAvoid = new DataItemDto();
        wordsToAvoid.setTitle("Words to avoid");
        wordsToAvoid.setInfoIcon(true);
        List<ContentItemDto> wordsToAvoidContent = new ArrayList<>();

        // Check for negative phrases or cliches in the resume
        boolean hasNegativePhrases = checkNegativePhrases(resume);
        ContentItemDto negativePhrasesContent = new ContentItemDto();
        negativePhrasesContent.setTickIcon(!hasNegativePhrases);
        if (hasNegativePhrases) {
            negativePhrasesContent.setContentValue("We've found some negative phrases or cliches in your resume. View negative words.");
        } else {
            negativePhrasesContent.setContentValue("Your resume does not contain negative phrases or cliches.");
        }
        wordsToAvoidContent.add(negativePhrasesContent);

        // Add words to avoid content to data item
        wordsToAvoid.setContent(wordsToAvoidContent);

        // Calculate weighted score for words to avoid
        double wordsToAvoidScore = calculateScore(wordsToAvoidContent);
        wordsToAvoid.setWeight(wordsToAvoidWeight * wordsToAvoidScore);

        // Add weighted words to avoid to data items list
        dataItems.add(wordsToAvoid);

        // Calculate total issues for words to avoid
        int wordsToAvoidIssues = countIssues(wordsToAvoidContent);


        // Add data items to the list
        dataItems.add(wordCount);
        dataItems.add(measurableResults);
        dataItems.add(jobLevelMatch);
        dataItems.add(wordsToAvoid);

        // Calculate total issues
        int totalIssues = jobLevelMatchIssues+measurableResultsIssues +wordCountIssues;


        // Calculate total weight
        double totalWeight = wordCountWeight + measurableResultsWeight + jobLevelMatchWeight + wordsToAvoidWeight;

        // Calculate overall percentage
        double overallPercentage = ((wordCountWeight * wordCountScore) + (measurableResultsWeight * measurableResultsScore))+(jobLevelMatchWeight*jobLevelMatchScore)+(wordsToAvoidWeight*wordsToAvoidScore) / totalWeight * 100;

        // Create SectionData object
        SectionDataDto sectionData = new SectionDataDto("Recruiter Tips", overallPercentage, totalIssues, dataItems);

        return sectionData;
    }
    public SectionDataDto generateHighlightsReport(String resume, String jobDescription) {
        List<DataItemDto> dataItems = new ArrayList<>();

        // Define weights for each section
        double webPresenceWeight = 1.0;

        // Compare web presence
        DataItemDto webPresence = new DataItemDto();
        webPresence.setTitle("Web Presence");
        webPresence.setInfoIcon(true);
        List<ContentItemDto> webPresenceContent = new ArrayList<>();

        // Check for web presence in the resume (e.g., links to portfolios, LinkedIn profile, personal website)
        boolean hasWebPresence = checkWebPresence(resume);
        ContentItemDto webPresenceItem = new ContentItemDto();
        webPresenceItem.setTickIcon(hasWebPresence);
        if (hasWebPresence) {
            webPresenceItem.setContentValue("Your resume includes web presence links.");
        } else {
            webPresenceItem.setContentValue("We did not find any web presence links in your resume.");
        }
        webPresenceContent.add(webPresenceItem);

        // Add web presence content to data item
        webPresence.setContent(webPresenceContent);

        // Calculate weighted score for web presence
        double webPresenceScore = calculateScore(webPresenceContent);
        webPresence.setWeight(webPresenceWeight * webPresenceScore);

        // Add weighted web presence to data items list
        dataItems.add(webPresence);

        // Calculate total issues for web presence
        int webPresenceIssues = countIssues(webPresenceContent);

        // Calculate total weight
        double totalWeight = webPresenceWeight;

        // Calculate overall percentage
        double overallPercentage = (webPresenceWeight * webPresenceScore) / totalWeight * 100;

        // Create SectionData object
        SectionDataDto sectionData = new SectionDataDto("Highlights", (int) overallPercentage, webPresenceIssues, dataItems);

        return sectionData;
    }
    public static SectionDataDto generateSoftSkillsReport(String resume, String jobDescription) {
        List<DataItemDto> dataItems = new ArrayList<>();

        // Define weights for each section
        double softSkillsWeight = 0.5;
        double wordCountWeight = 0.5;

        // Compare soft skills
        DataItemDto softSkills = new DataItemDto();
        softSkills.setTitle("Soft Skills");
        softSkills.setInfoIcon(true);
        List<ContentItemDto> softSkillsContent = new ArrayList<>();
        // Implement logic to check for soft skills
        boolean hasRequiredSoftSkills = checkSoftSkills(resume, jobDescription);
        ContentItemDto softSkillsItem = new ContentItemDto();
        softSkillsItem.setTickIcon(hasRequiredSoftSkills);
        if (hasRequiredSoftSkills) {
            softSkillsItem.setContentValue("You possess the required soft skills for this role.");
        } else {
            softSkillsItem.setContentValue("You may need to enhance your soft skills for this role.");
        }
        softSkillsContent.add(softSkillsItem);
        softSkills.setContent(softSkillsContent);
        double softSkillsScore = calculateScore(softSkillsContent);
        softSkills.setWeight(softSkillsWeight * softSkillsScore);
        dataItems.add(softSkills);

        // Calculate total issues for soft skills
        int softSkillsIssues = countIssues(softSkillsContent);

        // Compare word count
        DataItemDto wordCount = new DataItemDto();
        wordCount.setTitle("Word Count");
        wordCount.setInfoIcon(true);
        List<ContentItemDto> wordCountContent = new ArrayList<>();
        // Implement logic to check word count
        int totalWords = countWords(resume);
        ContentItemDto wordCountItem = new ContentItemDto();
        wordCountItem.setTickIcon(totalWords <= 1000); // Assuming the ideal word count is 1000 or less
        if (totalWords <= 1000) {
            wordCountItem.setContentValue("Your resume meets the ideal word count (1000 words or less).");
        } else {
            wordCountItem.setContentValue("Your resume exceeds the ideal word count (1000 words or less).");
        }
        wordCountContent.add(wordCountItem);
        wordCount.setContent(wordCountContent);
        double wordCountScore = calculateScore(wordCountContent);
        wordCount.setWeight(wordCountWeight * wordCountScore);
        dataItems.add(wordCount);

        // Calculate total issues for word count
        int wordCountIssues = countIssues(wordCountContent);

        // Calculate total weight
        double totalWeight = softSkillsWeight + wordCountWeight;

        // Calculate overall percentage
        double overallPercentage = ((softSkillsWeight * softSkillsScore) + (wordCountWeight * wordCountScore)) / totalWeight * 100;

        // Calculate total issues
        int totalIssues = softSkillsIssues + wordCountIssues;

        // Create SectionData object
        SectionDataDto sectionData = new SectionDataDto("Soft Skills", (int) overallPercentage, totalIssues, dataItems);

        return sectionData;
    }

    public SectionDataDto generateFormattingReport(String resume, String jobDescription) {
        List<DataItemDto> dataItems = new ArrayList<>();

        // Define weights for each section
        double fontCheckWeight = 0.25;
        double pageSetupWeight = 0.25;
        double layoutWeight = 0.25;
        double wordsToAvoidWeight = 0.25;

        // Compare font check
        DataItemDto fontCheck = new DataItemDto();
        fontCheck.setTitle("Font check");
        fontCheck.setInfoIcon(true);
        List<ContentItemDto> fontCheckContent = new ArrayList<>();
        // Implement logic to check font consistency
        boolean isFontConsistent = checkFontConsistency(resume);
        ContentItemDto fontCheckItem = new ContentItemDto();
        fontCheckItem.setTickIcon(isFontConsistent);
        if (isFontConsistent) {
            fontCheckItem.setContentValue("Font consistency is maintained throughout your resume.");
        } else {
            fontCheckItem.setContentValue("Font inconsistency found in your resume.");
        }
        fontCheckContent.add(fontCheckItem);
        fontCheck.setContent(fontCheckContent);
        double fontCheckScore = calculateScore(fontCheckContent);
        fontCheck.setWeight(fontCheckWeight * fontCheckScore);
        dataItems.add(fontCheck);

        // Calculate total issues for font check
        int fontCheckIssues = countIssues(fontCheckContent);

        // Compare page setup
        DataItemDto pageSetup = new DataItemDto();
        pageSetup.setTitle("Page Setup");
        pageSetup.setInfoIcon(true);
        List<ContentItemDto> pageSetupContent = new ArrayList<>();
        // Implement logic to check page setup
        boolean isPageSetupCorrect = checkPageSetup(resume);
        ContentItemDto pageSetupItem = new ContentItemDto();
        pageSetupItem.setTickIcon(isPageSetupCorrect);
        if (isPageSetupCorrect) {
            pageSetupItem.setContentValue("Page setup meets standard requirements.");
        } else {
            pageSetupItem.setContentValue("Page setup needs adjustment.");
        }
        pageSetupContent.add(pageSetupItem);
        pageSetup.setContent(pageSetupContent);
        double pageSetupScore = calculateScore(pageSetupContent);
        pageSetup.setWeight(pageSetupWeight * pageSetupScore);
        dataItems.add(pageSetup);

        // Calculate total issues for page setup
        int pageSetupIssues = countIssues(pageSetupContent);

        // Compare layout
        DataItemDto layout = new DataItemDto();
        layout.setTitle("Layout");
        layout.setInfoIcon(true);
        List<ContentItemDto> layoutContent = new ArrayList<>();
        // Implement logic to check layout
        boolean isLayoutCorrect = checkLayout(resume);
        ContentItemDto layoutItem = new ContentItemDto();
        layoutItem.setTickIcon(isLayoutCorrect);
        if (isLayoutCorrect) {
            layoutItem.setContentValue("Layout is well-structured and easy to read.");
        } else {
            layoutItem.setContentValue("Layout needs improvement for better readability.");
        }
        layoutContent.add(layoutItem);
        layout.setContent(layoutContent);
        double layoutScore = calculateScore(layoutContent);
        layout.setWeight(layoutWeight * layoutScore);
        dataItems.add(layout);

        // Calculate total issues for layout
        int layoutIssues = countIssues(layoutContent);

        // Compare words to avoid
        DataItemDto wordsToAvoid = new DataItemDto();
        wordsToAvoid.setTitle("Words to avoid");
        wordsToAvoid.setInfoIcon(true);
        List<ContentItemDto> wordsToAvoidContent = new ArrayList<>();
        // Implement logic to check for words to avoid
        boolean hasWordsToAvoid = checkWordsToAvoid(resume);
        ContentItemDto wordsToAvoidItem = new ContentItemDto();
        wordsToAvoidItem.setTickIcon(!hasWordsToAvoid);
        if (!hasWordsToAvoid) {
            wordsToAvoidItem.setContentValue("No negative phrases or cliches found in your resume.");
        } else {
            wordsToAvoidItem.setContentValue("Negative phrases or cliches found in your resume.");
        }
        wordsToAvoidContent.add(wordsToAvoidItem);
        wordsToAvoid.setContent(wordsToAvoidContent);
        double wordsToAvoidScore = calculateScore(wordsToAvoidContent);
        wordsToAvoid.setWeight(wordsToAvoidWeight * wordsToAvoidScore);
        dataItems.add(wordsToAvoid);

        // Calculate total issues for words to avoid
        int wordsToAvoidIssues = countIssues(wordsToAvoidContent);

        // Calculate total weight
        double totalWeight = fontCheckWeight + pageSetupWeight + layoutWeight + wordsToAvoidWeight;

        // Calculate overall percentage
        double overallPercentage = ((fontCheckWeight * fontCheckScore) + (pageSetupWeight * pageSetupScore)
                + (layoutWeight * layoutScore) + (wordsToAvoidWeight * wordsToAvoidScore)) / totalWeight * 100;

        // Calculate total issues
        int totalIssues = fontCheckIssues + pageSetupIssues + layoutIssues + wordsToAvoidIssues;

        // Create SectionData object
        SectionDataDto sectionData = new SectionDataDto("Formatting", (int) overallPercentage, totalIssues, dataItems);

        return sectionData;
    }
    public SectionDataDto generateHardSkillsReport(String resume, String jobDescription) {
        List<DataItemDto> dataItems = new ArrayList<>();

        // Define weights for each section
        double skillsWeight = 0.5;
        double wordCountWeight = 0.5;

        // Compare technical skills
        DataItemDto skills = new DataItemDto();
        skills.setTitle("Technical Skills");
        skills.setInfoIcon(true);
        List<ContentItemDto> skillsContent = new ArrayList<>();
        // Implement logic to check for specific technical skills
        boolean hasRequiredSkills = checkTechnicalSkills(resume, jobDescription);
        ContentItemDto skillsItem = new ContentItemDto();
        skillsItem.setTickIcon(hasRequiredSkills);
        if (hasRequiredSkills) {
            skillsItem.setContentValue("You possess the required technical skills for this role.");
        } else {
            skillsItem.setContentValue("You may need to enhance your technical skills for this role.");
        }
        skillsContent.add(skillsItem);
        skills.setContent(skillsContent);
        double skillsScore = calculateScore(skillsContent);
        skills.setWeight(skillsWeight * skillsScore);
        dataItems.add(skills);

        // Calculate total issues for technical skills
        int skillsIssues = countIssues(skillsContent);

        // Compare word count
        DataItemDto wordCount = new DataItemDto();
        wordCount.setTitle("Word Count");
        wordCount.setInfoIcon(true);
        List<ContentItemDto> wordCountContent = new ArrayList<>();
        // Implement logic to check word count
        int totalWords = countWords(resume);
        ContentItemDto wordCountItem = new ContentItemDto();
        wordCountItem.setTickIcon(totalWords <= 1000); // Assuming the ideal word count is 1000 or less
        if (totalWords <= 1000) {
            wordCountItem.setContentValue("Your resume meets the ideal word count (1000 words or less).");
        } else {
            wordCountItem.setContentValue("Your resume exceeds the ideal word count (1000 words or less).");
        }
        wordCountContent.add(wordCountItem);
        wordCount.setContent(wordCountContent);
        double wordCountScore = calculateScore(wordCountContent);
        wordCount.setWeight(wordCountWeight * wordCountScore);
        dataItems.add(wordCount);

        // Calculate total issues for word count
        int wordCountIssues = countIssues(wordCountContent);

        // Calculate total weight
        double totalWeight = skillsWeight + wordCountWeight;

        // Calculate overall percentage
        double overallPercentage = ((skillsWeight * skillsScore) + (wordCountWeight * wordCountScore)) / totalWeight * 100;

        // Calculate total issues
        int totalIssues = skillsIssues + wordCountIssues;

        // Create SectionData object
        SectionDataDto sectionData = new SectionDataDto("Hard Skills", (int) overallPercentage, totalIssues, dataItems);

        return sectionData;
    }


    // Method to calculate score based on content items
    private static double calculateScore(List<ContentItemDto> contentItems) {
        int totalItems = contentItems.size();
        int tickItems = 0;
        for (ContentItemDto item : contentItems) {
            if (item.isTickIcon()) {
                tickItems++;
            }
        }
        return (double) tickItems / totalItems; // Return ratio of ticked items

    }
    private static int countIssues(List<ContentItemDto> contentItems) {
        int issues = 0;
        for (ContentItemDto item : contentItems) {
            if (!item.isTickIcon()) {
                issues++;
            }
        }
        return issues;
    }
    private static boolean checkSeniorityMatch(String resume, String jobDescription) {
        // Implement logic to compare the seniority level in the resume with the job description
        // For example, compare job titles, years of experience, etc.
        return true; // Placeholder value for now
    }
    private static int countMeasurableResults(String resume) {
        // Implement counting of measurable results in resume
        // For example, count specific achievements or impact mentioned in the resume
        return 3; // Placeholder value for now
    }
    private static boolean checkNegativePhrases(String resume) {
        // Implement logic to scan the resume for negative phrases or cliches
        // For example, check for words like "lazy", "unmotivated", "bad communicator", etc.
        return false; // Placeholder value for now
    }
    private static boolean checkWebPresence(String resume) {
        // Implement logic to check for web presence in the resume
        // This could involve scanning for links to portfolios, LinkedIn profiles, personal websites, etc.
        return false; // Placeholder value for now
    }
    private static boolean checkFontConsistency(String resume) {
        // Implement logic to check font consistency
        // For example, you can check if the font style or size is consistent throughout the resume
        // Return true if font consistency is maintained, false otherwise
        // Placeholder implementation, replace with actual logic
        return true;
    }

    // Helper method to check page setup
    private static boolean checkPageSetup(String resume) {
        // Implement logic to check page setup
        // For example, you can check if the margins, spacing, and alignment are according to standard requirements
        // Return true if page setup meets requirements, false otherwise
        // Placeholder implementation, replace with actual logic
        return true;
    }

    // Helper method to check layout
    private static boolean checkLayout(String resume) {
        // Implement logic to check layout
        // For example, you can check if the sections are well-structured and easy to read
        // Return true if layout is well-structured, false otherwise
        // Placeholder implementation, replace with actual logic
        return true;
    }

    // Helper method to check for words to avoid
    private static boolean checkWordsToAvoid(String resume) {
        // Implement logic to check for words to avoid
        // For example, you can check for negative phrases or cliches that might weaken the resume
        // Return true if no negative phrases or cliches are found, false otherwise
        // Placeholder implementation, replace with actual logic
        return false;
    }
    private static boolean checkTechnicalSkills(String resume, String jobDescription) {
        // Define a set of required technical skills mentioned in the job description
        Set<String> requiredSkills = new HashSet<>(Arrays.asList("Java", "Spring Boot", "Hibernate", "AWS", "MySQL"));

        // Check if all required skills are present in the resume
        for (String skill : requiredSkills) {
            if (!resume.toLowerCase().contains(skill.toLowerCase())) {
                return false; // Skill not found in resume
            }
        }
        return true; // All skills found
    }

    // Helper method to count the number of words in the resume
    private static int countWords(String resume) {
        // Split the resume content by whitespace to count words
        String[] words = resume.split("\\s+");
        return words.length;
    }
    private static boolean checkSoftSkills(String resume, String jobDescription) {
        // Implement your logic here to check for soft skills
        // You can use regex, keyword matching, or any other method to identify soft skills
        // For example:
        // if (resume.contains("communication") && resume.contains("teamwork")) {
        //     return true;
        // } else {
        //     return false;
        // }
        return false; // Placeholder return statement
    }

    // Method to count the number of words in the resume


}
