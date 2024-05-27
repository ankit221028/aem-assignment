package com.aem.assignment.core.schedulers;

import com.aem.assignment.core.services.ContentCreationService;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Runnable.class, immediate = true)
public class ContentScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ContentScheduler.class);

    @Reference
    private Scheduler scheduler;

    @Reference
    private ContentCreationService contentCreationService;

    @Activate
    @Modified
    protected void activate() {
        ScheduleOptions scheduleOptions = scheduler.EXPR("");
        scheduler.schedule(this, scheduleOptions);
    }

    @Override
    public void run() {
        LOG.info("Running Content Scheduler");
        contentCreationService.pullAndCreatePages();
    }
}