package com.aem.assignment.core.services;

import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.taskmanagement.TaskManagerException;
import com.adobe.granite.taskmanagement.TaskManagerFactory;
import com.adobe.granite.workflow.exec.InboxItem;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

/**
 * A service for sending notifications using AEM's task management system.
 */
@Component(service = AemNotificationService.class, immediate = true)
public class AemNotificationService {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * Sends a notification with the specified title, description, and priority.
     *
     * @param title       the title of the notification
     * @param description the description of the notification
     * @param priority    the priority of the notification
     */
    public void sendNotification(String title, String description, InboxItem.Priority priority) {
        ResourceResolver resolver = null;

        try {
            resolver = getResolver();
            TaskManager taskManager = resolver.adaptTo(TaskManager.class);
            Task task = createTask(taskManager, title, description, priority);
            taskManager.createTask(task);
        } catch (TaskManagerException | LoginException e) {
            throw new RuntimeException("Failed to send notification", e);
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }

    /**
     * Creates a task with the specified details.
     *
     * @param taskManager the task manager
     * @param title       the title of the task
     * @param description the description of the task
     * @param priority    the priority of the task
     * @return the created task
     * @throws TaskManagerException if an error occurs while creating the task
     */
    private Task createTask(TaskManager taskManager, String title, String description, InboxItem.Priority priority) throws TaskManagerException {
        TaskManagerFactory taskManagerFactory = taskManager.getTaskManagerFactory();
        Task task = taskManagerFactory.newTask(Task.DEFAULT_TASK_TYPE);
        task.setName(title);
        task.setPriority(priority);
        task.setDescription(description);
        task.setCurrentAssignee("admin");
        return task;
    }

    /**
     * Retrieves a ResourceResolver using a system user.
     *
     * @return the resource resolver
     * @throws LoginException if an error occurs while retrieving the resolver
     */
    private ResourceResolver getResolver() throws LoginException {
        Map<String, Object> map = new HashMap<>();
        String SYSTEM_USER = "TestUser";
        map.put(ResourceResolverFactory.SUBSERVICE, SYSTEM_USER);
        return resourceResolverFactory.getServiceResourceResolver(map);
    }
}
