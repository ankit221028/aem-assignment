package com.aem.assignment.core.services.impl;

import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManagerFactory;
import com.day.cq.wcm.api.WCMException;
import com.aem.assignment.core.services.ContentCreationService;
import com.day.crx.JcrConstants;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.LoginException;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the ContentCreationService interface.
 * This service fetches content from a third-party API and creates corresponding AEM pages.
 */
@Component(service = ContentCreationService.class, immediate = true)
public class ContentCreationServiceImpl implements ContentCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentCreationServiceImpl.class);
    private static final String PARENT_PATH = "/content/aem_assignment/us/en";
    private static final String TEMPLATE_PATH = "/conf/aem_assignment/settings/wcm/templates/page-content";
    private static final String AUDIT_LOG_PATH = "/var/audit/content-creation";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Replicator replicator;

    @Reference
    private PageManagerFactory pageManagerFactory;

    /**
     * Fetches content from an API, creates AEM pages based on the content, and replicates the pages.
     */
    @Override
    public void pullAndCreatePages() {
        try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(getAuthInfo())) {

            // Fetch content from third-party API
            String apiResponse = fetchContentFromApi();

            // Parse the API response
            JSONObject contentJson = new JSONObject(apiResponse);

            // Create pages based on the content
            createPages(resolver, contentJson);

        } catch (LoginException | IOException | WCMException | JSONException | ReplicationException | RepositoryException e) {
            LOG.error("Error while creating pages: ", e);
        }
    }

    /**
     * Provides authentication information for obtaining a ResourceResolver.
     *
     * @return a Map containing authentication parameters.
     */
    private Map<String, Object> getAuthInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "TestUser");
        return paramMap;
    }

    /**
     * Fetches content from a third-party API.
     *
     * @return the content fetched from the API as a String.
     * @throws IOException if an I/O error occurs while fetching the content.
     */
    private String fetchContentFromApi() throws IOException {
        URL url = new URL("https://fakestoreapi.com/products/1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } finally {
            conn.disconnect();
        }
    }

    /**
     * Creates AEM pages based on the content JSON and replicates them.
     *
     * @param resolver    the ResourceResolver to use.
     * @param contentJson the content JSON fetched from the API.
     * @throws WCMException         if a WCM-related error occurs.
     * @throws PersistenceException if a persistence-related error occurs.
     * @throws JSONException        if a JSON parsing error occurs.
     * @throws ReplicationException if a replication-related error occurs.
     * @throws RepositoryException  if a repository-related error occurs.
     */
    private void createPages(ResourceResolver resolver, JSONObject contentJson) throws WCMException, PersistenceException, JSONException, ReplicationException, RepositoryException {
        PageManager pageManager = pageManagerFactory.getPageManager(resolver);
        String pageTitle = contentJson.getString("title");
        Page newPage = pageManager.create(PARENT_PATH, "TestingPage", TEMPLATE_PATH, "SchedulerPage");

        // Add content to the page
        addContentToPage(resolver, newPage, contentJson);

        // Replicate the page
        replicator.replicate(resolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, newPage.getPath());

        // Log the operation
        logAudit(resolver, newPage.getPath(), contentJson.toString(), "Page created and replicated successfully.");
    }

    /**
     * Adds content to the created AEM page.
     *
     * @param resolver    the ResourceResolver to use.
     * @param newPage     the newly created AEM page.
     * @param contentJson the content JSON fetched from the API.
     * @throws PersistenceException if a persistence-related error occurs.
     * @throws JSONException        if a JSON related error occurs.
     */
    private void addContentToPage(ResourceResolver resolver, Page newPage, JSONObject contentJson) throws PersistenceException, JSONException {
        Resource contentResource = resolver.getResource(newPage.getContentResource().getPath());
        assert contentResource != null;
        ModifiableValueMap properties = contentResource.adaptTo(ModifiableValueMap.class);
        assert properties != null;
        properties.put("jcr:title", contentJson.getString("title"));
        properties.put("productId", contentJson.getInt("id"));
        properties.put("price", contentJson.getDouble("price"));
        properties.put("description", contentJson.getString("description"));
        properties.put("category", contentJson.getString("category"));
        properties.put("image", contentJson.getString("image"));
        JSONObject rating = contentJson.getJSONObject("rating");
        properties.put("ratingRate", rating.getDouble("rate"));
        properties.put("ratingCount", rating.getInt("count"));

        // Save the changes
        resolver.commit();
    }

    /**
     * Logs the audit information for the content creation process.
     *
     * @param resolver    the ResourceResolver to use.
     * @param pagePath    the path of the created page.
     * @param apiResponse the response from the API.
     * @param status      the status message to log.
     * @throws PersistenceException if a persistence-related error occurs.
     * @throws RepositoryException  if a repository-related error occurs.
     */
    private void logAudit(ResourceResolver resolver, String pagePath, String apiResponse, String status) throws PersistenceException, RepositoryException {
        Resource auditLogResource = resolver.getResource(AUDIT_LOG_PATH);
        if (auditLogResource != null) {
            Node auditLogNode = auditLogResource.adaptTo(Node.class);
            if (auditLogNode != null) {
                Node logNode = auditLogNode.addNode("log-" + System.currentTimeMillis(), JcrConstants.NT_UNSTRUCTURED);
                logNode.setProperty("pagePath", pagePath);
                logNode.setProperty("apiResponse", apiResponse);
                logNode.setProperty("status", status);
                logNode.setProperty("timestamp", Calendar.getInstance());
                resolver.commit();
            }
        }
    }
}
