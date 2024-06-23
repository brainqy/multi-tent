package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.forum.ForumDto;
import com.yash.ytms.dto.QuestionDto;
import com.yash.ytms.services.IServices.ForumService;
import com.yash.ytms.services.IServices.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 24-04-2024
 */
@Component
public class ScheduledServiceImpl {
    @Autowired
    ForumService forumService;
    private final ChatClient chatClient;
    @Autowired
    QuestionService questionService;
    private static final Logger logger = LoggerFactory.getLogger(ScheduledServiceImpl.class);

    public ScheduledServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    //@Scheduled(fixedRate = 30000)
    public void createForumScheduledTask() {
        // Call the createForum method
        //System.out.println(generate("Interview Platform"));
        ForumDto forumDto = generate("Indian Geopolitics ");
System.out.println(forumDto.toString());
        // You can set other properties as needed
        forumService.createForum(forumDto);
    }

    public ForumDto generate(String title) {
        var outputParser = new BeanOutputParser<>(ForumDto.class);

        String userMessage =
                """
                                Generate a comprehensive blog post on the topic of {title} with a minimum word count of 1000 words. Utilize HTML tags to enhance the visual presentation and formatting of the content, ensuring a polished and professional appearance.
                                
                                Begin by introducing the topic and its relevance, encapsulating the introduction within <h2> tags to emphasize its importance and prominence.
                                
                                Next, explore various facets of the subject matter in-depth, breaking down the main theme into subtopics or key points. Utilize <h3> tags for subheadings to clearly delineate different sections and provide a logical structure to the content.
                                
                                For each subtopic, delve into the details with minimum 50 words explanations , utilizing <p> tags for paragraphs to present explanations, elaborations, and supporting evidence in minimum 50 words. Incorporate <ul> or <ol> tags for lists to organize information in a clear and concise manner.
                                
                                Enhance the visual appeal of the blog post by incorporating multimedia elements such as images, videos, or embedded content. Utilize <img> tags for images and <iframe> tags for embedded media, ensuring proper alignment and sizing for optimal display.
                                
                                Throughout the blog post, maintain consistency in formatting and styling to create a cohesive and visually appealing reading experience. Utilize CSS styles or inline styling within <style> tags to customize the appearance of text, headings, and other elements as needed.
                                
                                Encourage interaction and engagement by incorporating interactive elements such as hyperlinks, buttons, or call-to-action (CTA) prompts. Utilize <a> tags for hyperlinks and <button> tags for interactive buttons, linking to relevant resources or encouraging reader engagement.
                                
                                Conclude the blog post with a compelling closing statement or call-to-action, encapsulating the conclusion within <blockquote> tags to highlight its significance and impact.
                                
                                Upon completion, review the draft carefully to ensure it meets the minimum word count requirement while maintaining quality, relevance, and coherence. Make any necessary adjustments to formatting, styling, or content to enhance the overall readability and visual appeal of the blog post.
                                
                                
                                                                                                                                    
                        {format} 
                        """;

        PromptTemplate promptTemplate = new PromptTemplate(userMessage, Map.of("title", title, "format", outputParser.getFormat() ));
        Prompt prompt = promptTemplate.create();
        Generation generation = chatClient.call(prompt).getResult();

        ForumDto blogging = outputParser.parse(generation.getOutput().getContent());
        return blogging;
    }
    //@Scheduled(fixedRate = 10000)
    public void createQuestionScheduledTask() {
        QuestionDto questionDto = null;

            questionDto = generateQuestion("JAVA ,Springboot, Rest API ,Collections, multithreading,Spring Security,Java 8 Features, Microservices");
            System.out.println(questionDto.toString());
        try {
            // You can set other properties as needed
            questionService.createQuestion(questionDto);
            logger.info("Question creation scheduled task executed successfully.");
        } catch (Exception e) {
            logger.info("error creation this question ", questionDto);
            logger.error("Error occurred during question creation scheduled task.", e);
        }

    }

    private QuestionDto generateQuestion(String title) {
        var outputParser = new BeanOutputParser<>(QuestionDto.class);

        String userMessage =
                """
                Create a new question related to {title}:
                Type (choose one: multiple choice, true/false): {questionType}
                Question: {question}
                Options (for multiple choice, provide comma-separated options; e.g., A: Option 1, B: Option 2, C: Option 3, D: Option 4; for true/false, provide true/false): {options}
                Answer (choose one from the options): {answer}
                Explanation: {explanation}
                Tags: {tags}
                {format}
                """;

        // Provide actual values for template variables
        Map<String, Object> variables = Map.of(
                "title", title,
                "questionType", "",
                "question", "",
                "options", "",
                "answer", "",
                "explanation", "",
                "tags", "",
                "format", outputParser.getFormat()
        );

        PromptTemplate promptTemplate = new PromptTemplate(userMessage, variables);
        Prompt prompt = promptTemplate.create();
        Generation generation = chatClient.call(prompt).getResult();

        QuestionDto questionDto = outputParser.parse(generation.getOutput().getContent());
        return questionDto;
    }


}
