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

/**
 * Servlet to handle requests to a third-party API and return the response.
 */
@Component(service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/third-party-api"
        })
public class ApiServlet extends SlingAllMethodsServlet {

    private static final String API_URL = "https://v2.jokeapi.dev/joke/Any?idRange=5";

    /**
     * Handles HTTP GET requests by calling a third-party API and returning its response.
     *
     * @param request  the Sling HTTP request
     * @param response the Sling HTTP response
     * @throws IOException if an input or output exception occurred
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = createConnection(API_URL);
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                String jsonResponse = IOUtils.toString(connection.getInputStream(), "UTF-8");
                JSONObject data = parseJsonResponse(jsonResponse);
                response.getWriter().write(data.toString());
            } else {
                handleErrorResponse(response, statusCode);
            }
        } catch (JSONException e) {
            throw new RuntimeException("Error parsing JSON response", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Creates an HTTP connection to the specified URL.
     *
     * @param apiUrl the URL to connect to
     * @return the HTTP connection
     * @throws IOException if an input or output exception occurred
     */
    private HttpURLConnection createConnection(String apiUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");
        return connection;
    }

    /**
     * Parses the JSON response from a string.
     *
     * @param jsonResponse the JSON response as a string
     * @return the parsed JSONObject
     * @throws JSONException if a JSON parsing exception occurred
     */
    private JSONObject parseJsonResponse(String jsonResponse) throws JSONException {
        return new JSONObject(jsonResponse);
    }

    /**
     * Handles error responses by setting the appropriate status code and message.
     *
     * @param response   the Sling HTTP response
     * @param statusCode the HTTP status code
     * @throws IOException if an input or output exception occurred
     */
    private void handleErrorResponse(SlingHttpServletResponse response, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write("Error occurred while calling the API: " + statusCode);
    }
}
