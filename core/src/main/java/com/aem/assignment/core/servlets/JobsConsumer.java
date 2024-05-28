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

@Component(service = JobConsumer.class,property =
        {
                JobConsumer.PROPERTY_TOPICS+"=customjob"
        })
public class JobsConsumer implements  JobConsumer{
    Logger logger= LoggerFactory.getLogger(JobsConsumer.class);
    @Reference
    EmailService emailService;
    @Reference
    AemNotificationService aemNotificationService;
    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public JobResult process(Job job) {
        ResourceResolver resolver=null;
        try {
            resolver=getNewResolver();
            String path = (String) job.getProperty("path");
            String event = (String) job.getProperty("event");
            aemNotificationService.sendNotification("Page Detail","Page has been created", InboxItem.Priority.HIGH);
            emailService.sendEmail("gargshivam5023@gmail.com","sahankit.0722@gmail.com","Delete Node","Hello");

            logger.info("everything is ok!"+path);
            return JobResult.OK;
        }
        catch (Exception e){
//            e.printStackTrace();
            return JobResult.FAILED;
        }

    }
    public ResourceResolver getNewResolver() throws LoginException {
        Map<String,Object>map= new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE,"testuser");
        return resourceResolverFactory.getResourceResolver(map);
    }
}
