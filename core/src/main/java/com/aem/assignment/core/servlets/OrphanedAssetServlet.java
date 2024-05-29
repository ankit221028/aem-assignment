package com.aem.assignment.core.servlets;

import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Servlet to handle POST requests for retrieving child pages of a specified content path.
 */
@Component(service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/Myservlet"
        })
public class OrphanedAssetServlet extends SlingAllMethodsServlet {

    /**
     * Handles the HTTP POST method.
     *
     * @param request  the Sling HTTP request
     * @param response the Sling HTTP response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input or output error occurs
     */
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonInput = getRequestBody(request);
            JSONObject jsonObject = new JSONObject(jsonInput);
            String name = jsonObject.getString("name");

            String newPath = "/content/" + name;
            ResourceResolver resolver = request.getResourceResolver();

            List<String> dropDownList = getChildPages(resolver, newPath);

            if (dropDownList != null) {
                writeResponse(response, dropDownList);
            } else {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
            }
        } catch (JSONException e) {
            handleJSONException(response, e);
        } catch (Exception e) {
            handleGeneralException(response, e);
        }
    }

    /**
     * Retrieves the request body as a string.
     *
     * @param request the Sling HTTP request
     * @return the request body as a string
     * @throws IOException if an input or output error occurs
     */
    private String getRequestBody(SlingHttpServletRequest request) throws IOException {
        try (InputStream inputStream = request.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }

    /**
     * Retrieves the names of child pages of the specified path.
     *
     * @param resolver the resource resolver
     * @param path     the path to retrieve child pages from
     * @return a list of child page names, or null if the resource or page is not found
     */
    private List<String> getChildPages(ResourceResolver resolver, String path) {
        Resource resourcePath = resolver.getResource(path);
        if (resourcePath != null) {
            Page page = resourcePath.adaptTo(Page.class);
            if (page != null) {
                List<String> dropDownList = new ArrayList<>();
                Iterator<Page> iterator = page.listChildren();
                while (iterator.hasNext()) {
                    Page childPage = iterator.next();
                    dropDownList.add(childPage.getName());
                }
                return dropDownList;
            }
        }
        return null;
    }

    /**
     * Writes the response as a JSON array.
     *
     * @param response      the Sling HTTP response
     * @param dropDownList  the list of child page names
     * @throws IOException if an input or output error occurs
     */
    private void writeResponse(SlingHttpServletResponse response, List<String> dropDownList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dropDownList);

        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    /**
     * Handles JSONException by setting an appropriate response status and message.
     *
     * @param response the Sling HTTP response
     * @param e        the JSONException
     * @throws IOException if an input or output error occurs
     */
    private void handleJSONException(SlingHttpServletResponse response, JSONException e) throws IOException {
        response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Invalid JSON format: " + e.getMessage());
    }

    /**
     * Handles general exceptions by setting an appropriate response status and message.
     *
     * @param response the Sling HTTP response
     * @param e        the Exception
     * @throws IOException if an input or output error occurs
     */
    private void handleGeneralException(SlingHttpServletResponse response, Exception e) throws IOException {
        response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("An error occurred: " + e.getMessage());
    }
}
