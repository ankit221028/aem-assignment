package com.aem.assignment.core.servlets;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Event handler that listens for resource removal events and creates jobs accordingly.
 */
@Component(service = EventHandler.class, immediate = true, property = {
        EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/REMOVED",
        EventConstants.EVENT_FILTER + "=(path=/content/aem_assignment/us/en/*)"
})
public class JobsCreator implements EventHandler {

    @Reference
    private JobManager jobManager;

    /**
     * Handles the resource removal event and creates a job.
     *
     * @param event the event to handle
     */
    @Override
    public void handleEvent(Event event) {
        try {
            Map<String, Object> jobProperties = createJobProperties(event);
            createJob(jobProperties);
        } catch (Exception e) {
            e.printStackTrace(); // Consider logging the exception
        }
    }

    /**
     * Creates job properties from the event.
     *
     * @param event the event to extract properties from
     * @return a map containing job properties
     */
    private Map<String, Object> createJobProperties(Event event) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("path", event.getTopic());
        properties.put("event", event.getProperty(SlingConstants.PROPERTY_PATH));
        return properties;
    }

    /**
     * Creates a job with the given properties.
     *
     * @param jobProperties the properties to set for the job
     */
    private void createJob(Map<String, Object> jobProperties) {
        jobManager.addJob("customjob", jobProperties);
    }
}
