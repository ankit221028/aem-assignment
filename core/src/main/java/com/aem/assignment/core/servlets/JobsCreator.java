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

@Component(service= EventHandler.class,immediate = true,property = {
        EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/REMOVED",
        EventConstants.EVENT_FILTER + "=(path=/content/aem_assignment/us/en/*)"
})
public class JobsCreator implements  EventHandler{
    @Reference
    JobManager jobManager;
    @Override
    public void handleEvent(Event event) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("path", event.getTopic());
            map.put("event", event.getProperty(SlingConstants.PROPERTY_PATH));
            Job job = jobManager.addJob("customjob", map);
        }
        catch(Exception e){

        }
    }
}