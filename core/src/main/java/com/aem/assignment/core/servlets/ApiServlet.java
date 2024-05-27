package com.aem.assignment.core.servlets;


import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(service = Servlet.class,
            property = {
                    "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                    "sling.servlet.paths=" + "/bin/third-party-api"
            })
    public class ApiServlet extends SlingAllMethodsServlet {

        @Override
        protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
            // URL of the third-party API
            String apiUrl = "https://v2.jokeapi.dev/joke/Any?idRange=5";

            // Make the API call
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            // Process API response
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                String jsonResponse = IOUtils.toString(connection.getInputStream(), "UTF-8");
                JSONObject data = null;
                try {
                    data = new JSONObject(jsonResponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // Process data as needed
                response.getWriter().write(data.toString());
            } else {
                response.setStatus(statusCode);
                // Handle error
                response.getWriter().write("Error occurred while calling the API: " + statusCode);
            }
        }
    }
