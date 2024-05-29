package com.aem.assignment.core.servlets;

import com.adobe.granite.workflow.exec.InboxItem;
import com.aem.assignment.core.services.AemNotificationService;
import com.aem.assignment.core.services.EmailService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Job consumer that processes custom jobs by sending notifications and emails.
 */
@Component(service = JobConsumer.class, property = {
        JobConsumer.PROPERTY_TOPICS + "=customjob"
})
public class JobsConsumer implements JobConsumer {
    private static final Logger logger = LoggerFactory.getLogger(JobsConsumer.class);

    @Reference
    private EmailService emailService;

    @Reference
    private AemNotificationService aemNotificationService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * Processes the job by sending notifications and emails.
     *
     * @param job the job to process
     * @return the result of job processing
     */
    @Override
    public JobResult process(Job job) {
        try (ResourceResolver resolver = getNewResolver()) {
            String path = (String) job.getProperty("path");
            String event = (String) job.getProperty("event");

            sendNotification("Page Detail", "Page has been created", InboxItem.Priority.HIGH);
            sendEmail("gargshivam5023@gmail.com", "sahankit.0722@gmail.com", "Delete Node", "Hello");

            logger.info("Job processed successfully: {}", path);
            return JobResult.OK;
        } catch (Exception e) {
            logger.error("Job processing failed", e);
            return JobResult.FAILED;
        }
    }

    /**
     * Retrieves a new resource resolver.
     *
     * @return a new resource resolver
     * @throws LoginException if login fails
     */
    private ResourceResolver getNewResolver() throws LoginException {
        Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, "testuser");
        return resourceResolverFactory.getResourceResolver(params);
    }

    /**
     * Sends a notification.
     *
     * @param title       the title of the notification
     * @param description the description of the notification
     * @param priority    the priority of the notification
     */
    private void sendNotification(String title, String description, InboxItem.Priority priority) {
        aemNotificationService.sendNotification(title, description, priority);
    }

    /**
     * Sends an email.
     *
     * @param to      the recipient's email address
     * @param from    the sender's email address
     * @param subject the subject of the email
     * @param text    the text of the email
     */
    private void sendEmail(String to, String from, String subject, String text) {
        emailService.sendEmail(to, from, subject, text);
    }
}
