package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.forum.ForumDto;
import com.yash.ytms.services.IServices.ForumService;
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

}
