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

/**
 * Scheduler component that periodically triggers content creation.
 * Implements Runnable to define the task that will be scheduled.
 */
@Component(service = Runnable.class, immediate = true)
public class ContentScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ContentScheduler.class);

    @Reference
    private Scheduler scheduler;

    @Reference
    private ContentCreationService contentCreationService;

    /**
     * Activates or modifies the scheduler configuration.
     * Schedules the task using the provided cron expression.
     */
    @Activate
    @Modified
    protected void activate() {
        String cronExpression = getCronExpression();
        ScheduleOptions scheduleOptions = scheduler.EXPR(cronExpression);
        scheduler.schedule(this, scheduleOptions);
    }

    /**
     * The task that will be executed according to the schedule.
     * Triggers the content creation process.
     */
    @Override
    public void run() {
        LOG.info("Running Content Scheduler");
        contentCreationService.pullAndCreatePages();
    }

    /**
     * Provides the cron expression for the scheduler.
     * This method can be modified to fetch the expression from a configuration or other source.
     *
     * @return the cron expression as a string.
     */
    private String getCronExpression() {
        // Define the cron expression or fetch it from a configuration
        return "0 0 * * * ?"; // Example: runs every hour at the start of the hour
    }
}
