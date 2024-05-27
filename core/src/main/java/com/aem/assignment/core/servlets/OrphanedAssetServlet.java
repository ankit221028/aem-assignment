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

@Component(service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/Myservlet"
        })
public class OrphanedAssetServlet extends SlingAllMethodsServlet {
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try (InputStream inputStream = request.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String jsonInput = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(jsonInput);
            String name = jsonObject.getString("name");

            String newPath = "/content/" + name;
            ResourceResolver resolver = request.getResourceResolver();

            List<String> dropDownList = new ArrayList<>();
            Resource resourcePath = resolver.getResource(newPath);

            if (resourcePath != null) {
                Page page = resourcePath.adaptTo(Page.class);
                if (page != null) {
                    Iterator<Page> iterator = page.listChildren();
                    while (iterator.hasNext()) {
                        Page childPage = iterator.next();
                        dropDownList.add(childPage.getName());
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(dropDownList);

                    response.setContentType("application/json");
                    response.getWriter().write(json);
                } else {
                    response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
            }
        } catch (JSONException e) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }
}
