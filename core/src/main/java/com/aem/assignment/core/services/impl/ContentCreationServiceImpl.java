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

    @Override
    public void pullAndCreatePages() {
        ResourceResolver resolver = null;
        try {
            resolver = resourceResolverFactory.getServiceResourceResolver(getAuthInfo());

            // Fetch content from third-party API
            String apiResponse = fetchContentFromApi();

            // Parse the API response
            JSONObject contentJson = new JSONObject(apiResponse);

            // Create pages based on the content
            createPages(resolver, contentJson);

        } catch (LoginException | IOException | WCMException | JSONException | ReplicationException |
                 RepositoryException e) {
            LOG.error("Error while creating pages: ", e);
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }

    private Map<String, Object> getAuthInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "TestUser");
        return paramMap;
    }

    private String fetchContentFromApi() throws IOException {
        URL url = new URL("https://fakestoreapi.com/products/1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        return content.toString();
    }

    private void createPages(ResourceResolver resolver, JSONObject contentJson) throws WCMException, PersistenceException, JSONException, ReplicationException, RepositoryException {
        PageManager pageManager = pageManagerFactory.getPageManager(resolver);
        String pageTitle = contentJson.getString("title");
        String pageName = pageTitle.toLowerCase().replace(" ", "-");
        Page newPage = pageManager.create(PARENT_PATH, pageName, TEMPLATE_PATH, pageTitle);

        // Add content to the page
        Resource contentResource = resolver.getResource(newPage.getContentResource().getPath());
        ModifiableValueMap properties = contentResource.adaptTo(ModifiableValueMap.class);
        properties.put("jcr:title", pageTitle);
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

        // Replicate the page
        replicator.replicate(resolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, newPage.getPath());

        // Log the operation
        logAudit(resolver, newPage.getPath(), contentJson.toString(), "Page created and replicated successfully.");
    }

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
